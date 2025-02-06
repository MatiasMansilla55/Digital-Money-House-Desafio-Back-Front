package com.example.api_activity.controller;

import com.example.api_activity.dto.entry.ActivityFilterEntryDTO;
import com.example.api_activity.dto.exit.ActivityOutDTO;
import com.example.api_activity.dto.exit.TransferenceOutDTO;
import com.example.api_activity.entities.Activity;
import com.example.api_activity.exceptions.InvalidTokenException;
import com.example.api_activity.exceptions.ResourceNotFoundException;
import com.example.api_activity.exceptions.UnauthorizedException;
import com.example.api_activity.feign.TransfersFeignClient;
import com.example.api_activity.repository.ActivityRepository;
import com.example.api_activity.service.impl.ActivityServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/accounts/{accountId}/activity")
public class ActivityController {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ActivityServiceImpl activityServiceImpl;
    @Autowired
    private TransfersFeignClient transfersFeignClient;
    @Operation(summary = "Obtener Todas las actividades")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Actividades Obtenidas con exito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActivityOutDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<?> getAllActivities(@PathVariable Long accountId, @RequestHeader("Authorization") String token) {
        try {
            // No se pasa el token, ya que la validación se realiza en el filtro
            List<ActivityOutDTO> activities = activityServiceImpl.getAllActivitiesByAccountId(accountId, token);
            return ResponseEntity.ok(activities); // 200 OK
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(403).body("Sin permisos"); // 403 Forbidden
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body("Cuenta no encontrada"); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Bad request"); // 400 Bad Request
        }
    }

    @Operation(summary = "Obtener el detalle de la actividad ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Detalle de la Actividad obtenida con exito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Activity.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @GetMapping("/{activityId}")
    public ResponseEntity<?> getActivityDetail(
            @PathVariable Long accountId,
            @PathVariable Long activityId,
            @RequestHeader("Authorization") String token) {
        try {
            ActivityOutDTO activity = activityServiceImpl.getActivityDetail(accountId, activityId);
            return ResponseEntity.ok(activity); // 200 OK
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(403).body("Sin permisos"); // 403 Forbidden
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body("TransferID inexistente"); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Bad request"); // 400 Bad Request
        }
    }
    @Operation(summary = "Filtrar actividades")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Actividades filtradas con exito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActivityOutDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @PostMapping("/filter")
    public ResponseEntity<List<ActivityOutDTO>> filterActivities(
            @PathVariable Long accountId,
            @RequestBody ActivityFilterEntryDTO filter) {
        List<ActivityOutDTO> activities = activityServiceImpl.filterActivities(filter);
        return ResponseEntity.ok(activities); // 200 OK
    }
    @Operation(summary = "Generar comprobante de actividad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comprobante generado con exito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ByteArrayOutputStream.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @GetMapping("/{activityId}/receipt")
    public ResponseEntity<byte[]> downloadActivityReceipt(@PathVariable Long accountId, @PathVariable Long activityId) throws ResourceNotFoundException {
        Activity activity = activityServiceImpl.getActivityById(activityId);
        ByteArrayOutputStream pdfStream = activityServiceImpl.generateActivityReceipt(activity);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comprobante_operacion.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }
    //Endpoint para Feign:
    @PostMapping("/save")
    public ResponseEntity<Activity> saveActivity(@RequestBody Activity account, @PathVariable Long accountId,@RequestHeader("Authorization") String token) {
        Activity savedActivity = activityRepository.save(account);
        return ResponseEntity.ok(savedActivity);
    }

    /**
    @PostMapping("/transferences/cards")
    public ResponseEntity<String> registerTransference(
            @PathVariable Long accountId,
            @RequestBody TransferenceOutDTO transferenceOutDto, @RequestHeader("Authorization") String token) {

        try {
            transfersFeignClient.registerTransference(accountId, transferenceOutDto,token);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ingreso registrado con éxito");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta o tarjeta no encontrada");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sin permisos");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar el ingreso");
        }
    }
            **/
}