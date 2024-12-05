import React, { useState } from 'react';
import { useForm, SubmitHandler } from 'react-hook-form';
import Button from '@mui/material/Button';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import OutlinedInput from '@mui/material/OutlinedInput';
import { SnackBar } from '../../components';
import { ERROR_MESSAGES, SUCCESS_MESSAGES } from '../../constants/';
import { resetPassword } from '../../utils';
import { useNavigate, useLocation } from 'react-router-dom';

interface ResetPasswordInputs {
  newPassword: string;
  confirmPassword: string;
}

const ResetPassword: React.FC = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ResetPasswordInputs>();

  const [isSuccess, setIsSuccess] = useState<boolean>(false);
  const [isError, setIsError] = useState<boolean>(false);
  const [message, setMessage] = useState<string>('');
  const navigate = useNavigate();
  const location = useLocation();
  
  // Obtener el token de la URL
  const params = new URLSearchParams(location.search);
  const token = params.get('token');

  // Redirigir a login si no hay token
  if (!token) {
    setIsError(true);
    setMessage('El enlace de restablecimiento es inválido o ha expirado.');
    setTimeout(() => {
      navigate('/login');
    }, 2000);
    return null;
  }

  const onSubmit: SubmitHandler<ResetPasswordInputs> = async ({ newPassword, confirmPassword }) => {
    setMessage('');
    setIsError(false);
    setIsSuccess(false);

    // Validar que las contraseñas coincidan
    if (newPassword !== confirmPassword) {
      setIsError(true);
      setMessage('Las contraseñas no coinciden');
      return;
    }

    try {
      // Llama a la función para restablecer la contraseña
      await resetPassword(token, newPassword, confirmPassword);
      setIsSuccess(true);
      setMessage(SUCCESS_MESSAGES.PASSWORD_RESET_SUCCESSFUL);
      
      // Redirige a la página de inicio de sesión después de 2 segundos
      setTimeout(() => {
        navigate('/login');
      }, 2000);
    } catch (error) {
      setIsError(true);
      setMessage(ERROR_MESSAGES.PASSWORD_RESET_FAILED);
    }
  };

  return (
    <div className="tw-w-full tw-h-full tw-flex tw-flex-col tw-flex-1 tw-items-center tw-justify-center">
      <h2 className="text-center">Restablecer la Contraseña</h2>
      <form onSubmit={handleSubmit(onSubmit)} className="tw-flex tw-flex-col tw-gap-y-4 w-full max-w-md">
        <FormControl variant="outlined" error={Boolean(errors.newPassword)}>
          <InputLabel htmlFor="outlined-new-password">Nueva Contraseña</InputLabel>
          <OutlinedInput
            id="outlined-new-password"
            type="password"
            {...register('newPassword', { required: 'Este campo es requerido' })}
            label="Nueva Contraseña"
          />
        </FormControl>
        {errors.newPassword && <p className="tw-text-red-600">{errors.newPassword.message}</p>}
        
        <FormControl variant="outlined" error={Boolean(errors.confirmPassword)}>
          <InputLabel htmlFor="outlined-confirm-password">Confirmar Contraseña</InputLabel>
          <OutlinedInput
            id="outlined-confirm-password"
            type="password"
            {...register('confirmPassword', { required: 'Este campo es requerido' })}
            label="Confirmar Contraseña"
          />
        </FormControl>
        {errors.confirmPassword && <p className="tw-text-red-600">{errors.confirmPassword.message}</p>}
        
        <Button type="submit" variant="outlined">
          Restablecer Contraseña
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

export default ResetPassword;
