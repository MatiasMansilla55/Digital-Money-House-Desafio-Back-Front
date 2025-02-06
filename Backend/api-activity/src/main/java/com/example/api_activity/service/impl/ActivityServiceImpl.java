package com.example.api_activity.service.impl;

import com.example.api_activity.dto.entry.Account;
import com.example.api_activity.dto.entry.ActivityFilterEntryDTO;
import com.example.api_activity.dto.exit.AccountOutDTO;
import com.example.api_activity.dto.exit.ActivityOutDTO;
import com.example.api_activity.entities.Activity;
import com.example.api_activity.exceptions.ResourceNotFoundException;
import com.example.api_activity.feign.AccountFeignClient;
import com.example.api_activity.repository.ActivityRepository;

import com.example.api_activity.service.IActivityService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl implements IActivityService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private AccountFeignClient accountFeignClient;
    @Autowired
    public ActivityServiceImpl(ModelMapper modelMapper,AccountFeignClient accountFeignClient,ActivityRepository activityRepository) {
        this.modelMapper = modelMapper;
        this.accountFeignClient=accountFeignClient;
        this.activityRepository=activityRepository;


    }

    // Obtener todas las actividades por cuenta, ordenadas por fecha (descendente)
    public List<ActivityOutDTO> getAllActivitiesByAccountId(Long accountId, String token) throws ResourceNotFoundException {
        // Buscar la cuenta por ID

            // Llama al microservicio Account-Service
        AccountOutDTO account = accountFeignClient.getAccountById(accountId,token)
                   .orElseThrow(() -> new ResourceNotFoundException("Account not found"));



        // Obtener las actividades de la cuenta
        List<Activity> activities = activityRepository.findAllByAccountIdOrderByDateDesc(accountId);

        // Convertir las entidades Activity a DTO
        return activities.stream()
                .map(activity ->modelMapper.map(activity, ActivityOutDTO.class))  // mapea la lista y Devuelve un coleccion de lista
                .collect(Collectors.toList());
    }

    // Obtener el detalle de una actividad específica (activityId)
    public ActivityOutDTO getActivityDetail(Long accountId, Long id)
            throws ResourceNotFoundException {

        // Buscar la actividad por accountId y activityId
        Activity activity = activityRepository.findByAccountIdAndId(accountId, id)
                .orElseThrow(() -> new ResourceNotFoundException("ActivityID inexistente"));

        // Convertir la actividad a DTO
        return modelMapper.map(activity, ActivityOutDTO.class);
    }

    public List<ActivityOutDTO> filterActivities(ActivityFilterEntryDTO filter) {
        // Establecer valores predeterminados para montos si no se proporcionan
        BigDecimal minAmount = filter.getMinAmount() != null ? filter.getMinAmount() : BigDecimal.ZERO;
        BigDecimal maxAmount = filter.getMaxAmount() != null ? filter.getMaxAmount() : BigDecimal.valueOf(1000000000);

        LocalDate startDate = filter.getStartDate();
        LocalDate endDate = filter.getEndDate();
        String activityType = filter.getActivityType();

        // Llamar al repositorio con los filtros aplicados
        List<Activity> activities = activityRepository.filterActivities(
                minAmount,
                maxAmount,
                startDate,
                endDate,
                activityType
        );

        return activities.stream()
                .map(activity -> modelMapper.map(activity, ActivityOutDTO.class))
                .collect(Collectors.toList());
    }

    public Activity getActivityById(Long activityId) throws ResourceNotFoundException {
        return activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));
    }

    // Método para generar el comprobante de actividad en PDF
    public ByteArrayOutputStream generateActivityReceipt(Activity activity) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Agregar información de la actividad al PDF
            document.add(new Paragraph("Comprobante de Actividad"));
            document.add(new Paragraph("ID de Actividad: " + activity.getId()));
            document.add(new Paragraph("Tipo de Actividad: " + activity.getType()));
            document.add(new Paragraph("Monto: " + activity.getAmount()));
            document.add(new Paragraph("Fecha: " + activity.getDate().toString()));
            document.add(new Paragraph("Descripción: " + activity.getDescription()));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream;
    }



}
