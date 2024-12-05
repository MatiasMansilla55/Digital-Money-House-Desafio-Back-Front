import React, { useState } from 'react';
import { useForm, SubmitHandler } from 'react-hook-form';
import Button from '@mui/material/Button';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import OutlinedInput from '@mui/material/OutlinedInput';
import { SnackBar } from '../../components';
import { ERROR_MESSAGES, SUCCESS_MESSAGES } from '../../constants/';
import { sendPasswordResetEmail } from '../../utils';
import { useNavigate } from 'react-router-dom';
import { ROUTES } from '../../constants';

interface RecoverPasswordInputs {
  email: string; // Campo de email
}

const RecoverPassword: React.FC = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<RecoverPasswordInputs>();

  const [isSuccess, setIsSuccess] = useState<boolean>(false);
  const [isError, setIsError] = useState<boolean>(false);
  const [message, setMessage] = useState<string>('');

  const navigate = useNavigate(); // Crea la instancia de navigate

  const onSubmit: SubmitHandler<RecoverPasswordInputs> = async ({ email }) => {
    // Restablecer el estado del mensaje y el error al intentar enviar
    setMessage('');
    setIsError(false);
    setIsSuccess(false);

    try {
      await sendPasswordResetEmail(email);
      setIsSuccess(true);
      setMessage(SUCCESS_MESSAGES.PASSWORD_RESET_EMAIL_SENT);

      // Redirige al login después de 2 segundos
      setTimeout(() => {
        navigate(ROUTES.LOGIN);
      }, 2000);
    } catch (error) {
      console.error("Error capturado:", error);
      setIsError(true);
      setMessage(ERROR_MESSAGES.EMAIL_NOT_FOUND);

      // Log para verificar el mensaje
      console.log("Mensaje de error establecido:", ERROR_MESSAGES.EMAIL_NOT_FOUND);
    }
  };

  return (
    <div className="tw-w-full tw-h-full tw-flex tw-flex-col tw-flex-1 tw-items-center tw-justify-center">
      <h2 className="text-center">Recupera tu contraseña</h2>
      <h2 className="text-center mb-4">
        Ingresa tu correo electrónico para recibir instrucciones
      </h2><br />
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
        <Button type="submit" variant="outlined">
          Enviar
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

export default RecoverPassword;
