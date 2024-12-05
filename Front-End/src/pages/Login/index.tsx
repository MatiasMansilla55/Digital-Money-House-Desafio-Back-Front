import React, { useState, useMemo, useEffect } from 'react';
import { useForm, SubmitHandler } from 'react-hook-form';
import Button from '@mui/material/Button';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputAdornment from '@mui/material/InputAdornment';
import IconButton from '@mui/material/IconButton';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import {
  isValueEmpty,
  valuesHaveErrors,
  emailValidationConfig,
  passwordValidationConfig,
  handleChange,
  login,
} from '../../utils/';
import { ErrorMessage, Errors } from '../../components/ErrorMessage';
import { useAuth, useLocalStorage } from '../../hooks';
import { SnackBar } from '../../components';
import { ERROR_MESSAGES } from '../../constants';
import { useNavigate } from 'react-router-dom';

interface LoginState {
  email: string;
  password: string;
  showPassword: boolean;
}

export interface LoginInputs {
  email: string;
  password: string;
}

const messageDuration = 2000;

const Login = () => {
  const {
    register,
    handleSubmit,
    formState: { errors, isDirty },
  } = useForm<LoginInputs>({
    criteriaMode: 'all',
  });

  const [token, setToken] = useLocalStorage('token');
  const [values, setValues] = useState<LoginState>({
    email: '',
    password: '',
    showPassword: false,
  });
  const { setIsAuthenticated } = useAuth();
  const [isError, setIsError] = useState<boolean>(false);
  const [isSubmiting, setIsSubmiting] = useState<boolean>(false);
  const [message, setMessage] = useState<string>('');

  const isEmpty = isValueEmpty(values);
  const hasErrors = useMemo(() => valuesHaveErrors(errors), [errors]);

  const navigate = useNavigate(); // Replacing useHistory with useNavigate

  const handleClickShowPassword = () => {
    setValues({
      ...values,
      showPassword: !values.showPassword,
    });
  };

  const handleMouseDownPassword = (
    event: React.MouseEvent<HTMLButtonElement>
  ) => {
    event.preventDefault();
  };

  const onChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    maxLength?: number
  ) => handleChange<LoginState>(event, setValues, maxLength);

  const onSubmit: SubmitHandler<LoginInputs> = ({ email, password }) => {
    setIsSubmiting(true);
    login(email, password)
      .then((response) => {
        setToken(response.token);
        setTimeout(() => {
          setIsSubmiting(false);
          setIsAuthenticated(true);
          navigate('/dashboard'); // Redirecting to the dashboard or desired page after login
        }, messageDuration);
      })
      .catch((error) => {
        setIsSubmiting(false);
        if (error.status === 403 && error.statusText === 'Forbidden') {
          // Redirigir a la página de confirmación de registro
          navigate('/confirm-registration');
        } // Manejar errores 500 (Internal Server Error) o 400 (Bad Request)
        else if (error.status === 404) {
          setMessage(ERROR_MESSAGES.NOT_FOUND_USER);
          setIsError(true); // Mostrar el mensaje de error en pantalla
        } 
        // Manejo de cualquier otro tipo de error
        else {
          setMessage(ERROR_MESSAGES.INVALID_PASSWORD);
          setIsError(true);
        }
      });
  };

  // Resetear el mensaje después de que se muestra o al cerrar SnackBar
  useEffect(() => {
    if (message) {
      const timer = setTimeout(() => {
        setMessage(''); // Resetea el mensaje después de un tiempo
      }, 3000); // 3 segundos de duración
      return () => clearTimeout(timer); // Limpiar timeout al desmontar el componente
    }
  }, [message]);

  return (
    <div
      className="tw-w-full tw-flex tw-flex-col tw-flex-1 tw-items-center tw-justify-center"
      style={{
        height: 'calc(100vh - 128px)',
      }}
    >
      <h2>Iniciar sesión</h2>
      <form
        className="tw-flex tw-flex-col tw-gap-y-12 tw-mt-10"
        onSubmit={handleSubmit(onSubmit)}
      >
        <div>
          <FormControl variant="outlined">
            <InputLabel htmlFor="outlined-adornment-password">
              Correo
            </InputLabel>
            <OutlinedInput
              id="outlined-adornment-email"
              type="text"
              value={values.email}
              {...register('email', emailValidationConfig)}
              onChange={onChange}
              label="email"
              autoComplete="off"
            />
          </FormControl>
          {errors.email && <ErrorMessage errors={errors.email as Errors} />}
        </div>
        <div>
          <FormControl variant="outlined">
            <InputLabel htmlFor="outlined-adornment-password">
              Contraseña
            </InputLabel>
            <OutlinedInput
              id="outlined-adornment-password"
              type={values.showPassword ? 'text' : 'password'}
              value={values.password}
              {...register('password', passwordValidationConfig)}
              onChange={onChange}
              endAdornment={
                <InputAdornment position="end">
                  <IconButton
                    aria-label="toggle password visibility"
                    onClick={handleClickShowPassword}
                    onMouseDown={handleMouseDownPassword}
                    edge="end"
                    className="tw-text-neutral-gray-100"
                  >
                    {values.showPassword ? <VisibilityOff /> : <Visibility />}
                  </IconButton>
                </InputAdornment>
              }
              label="Password"
              autoComplete="off"
            />
          </FormControl>
          {errors.password && (
            <ErrorMessage errors={errors.password as Errors} />
          )}
        </div>
        <Button
          className={`tw-h-14 ${
            hasErrors || !isDirty || isEmpty || isSubmiting
              ? 'tw-text-neutral-gray-300 tw-border-neutral-gray-300 tw-cursor-not-allowed'
              : ''
          }`}
          type="submit"
          variant="outlined"
          disabled={hasErrors || !isDirty || isEmpty || isSubmiting}
        >
          Ingresar
        </Button>
      </form>
      <div style={{ fontSize: '0.8rem', marginTop: '1rem' }}>
        <a href="/recover-password" style={{ textDecoration: 'none', color: '#007bff' }}>
          Olvidaste tu contraseña?
        </a>
      </div>
      {message.length > 0 && (
        <SnackBar
          duration={messageDuration}
          message={message}
          type={isError ? 'error' : 'primary'}
        />
      )}
    </div>
  );
};

export default Login;
