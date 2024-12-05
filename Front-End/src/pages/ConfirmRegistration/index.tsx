import React, { useState } from 'react';
import { useForm, SubmitHandler } from 'react-hook-form';
import Button from '@mui/material/Button';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import OutlinedInput from '@mui/material/OutlinedInput';
import { SnackBar } from '../../components';
import { ERROR_MESSAGES, SUCCESS_MESSAGES } from '../../constants/';
import { confirmUserRegistration } from '../../utils';
import { useNavigate } from 'react-router-dom';
import { ROUTES } from '../../constants';

interface ConfirmInputs {
  confirmationCode: string;
  email: string; // Añadir el campo de email
}

const ConfirmRegistration: React.FC = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ConfirmInputs>();

  const [isSuccess, setIsSuccess] = useState<boolean>(false);
  const [isError, setIsError] = useState<boolean>(false);
  const [message, setMessage] = useState<string>('');

  const navigate = useNavigate(); // Crea la instancia de navigate

  const onSubmit: SubmitHandler<ConfirmInputs> = async ({ confirmationCode, email }) => {
    // Restablecer el estado del mensaje y el error al intentar enviar
    setMessage('');
    setIsError(false);
    setIsSuccess(false);
  
    try {
      await confirmUserRegistration(confirmationCode, email);
      setIsSuccess(true);
      setMessage(SUCCESS_MESSAGES.REGISTRATION_CONFIRMED);
  
      // Redirige al login después de 2 segundos
      setTimeout(() => {
        navigate(ROUTES.LOGIN);
      }, 2000);
    } catch (error) {
      console.error("Error capturado:", error);
      setIsError(true);
      setMessage(ERROR_MESSAGES.INVALID_CONFIRMATION_CODE);
  
      // Log para verificar el mensaje
      console.log("Mensaje de error establecido:", ERROR_MESSAGES.INVALID_CONFIRMATION_CODE);
    }
  };
  
  return (
    <div className="tw-w-full tw-h-full tw-flex tw-flex-col tw-flex-1 tw-items-center tw-justify-center">
      <h2 className="text-center">Confirma tu registro con el</h2>
      <h2 className="text-center mb-4">
        código que te hemos enviado por email
      </h2>
      <span>&nbsp;</span>
      <form onSubmit={handleSubmit(onSubmit)} className="tw-flex tw-flex-col tw-gap-y-4 w-full max-w-md">
        <FormControl variant="outlined" error={Boolean(errors.email)}>
          <InputLabel htmlFor="outlined-email">Email</InputLabel>
          <OutlinedInput
            id="outlined-email"
            type="email"
            {...register('email', { required: 'Este campo es requerido' })}
            label="Email"
          />
        </FormControl>
        {errors.email && (
          <p className="tw-text-red-600">{errors.email.message}</p>
        )}
        <FormControl variant="outlined" error={Boolean(errors.confirmationCode)}>
          <InputLabel htmlFor="outlined-confirmation-code">Código de Confirmación</InputLabel>
          <OutlinedInput
            id="outlined-confirmation-code"
            type="text"
            {...register('confirmationCode', { required: 'Este campo es requerido' })}
            label="Código de Confirmación"
          />
        </FormControl>
        {errors.confirmationCode && (
          <p className="tw-text-red-600">{errors.confirmationCode.message}</p>
        )}
        <Button type="submit" variant="outlined">
          Confirmar
        </Button>
      </form>
      {message && (
        <SnackBar
          duration={2000}
          message={message}
          type={isSuccess ? 'success' : isError ? 'error' : undefined}
        />
      )}
    </div>
  );    
};

export default ConfirmRegistration;

