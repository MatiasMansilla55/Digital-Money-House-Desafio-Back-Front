// constants/index.ts
import { ActivityType } from '../types';

export const ERROR_MESSAGES: Record<string, string> = {
  INVALID_EMAIL: 'Correo electrónico inválido',
  INVALID_PASSWORD: 'Debe tener un número, una letra may. y una min.',
  PASSWORDS_DO_NOT_MATCH: 'Las contraseñas no coinciden',
  INVALID_NAME: 'Nombre inválido',
  INVALID_PHONE: 'Teléfono inválido',
  INVALID_DNI: 'DNI inválido',
  INVALID_CARD: 'Tarjeta inválida',
  INVALID_EXPIRATION: 'Fecha de expiración inválida',
  INVALID_CVC: 'CVC debe tener al menos 3 dígitos',
  INVALID_ALIAS: 'El alias deben ser 3 palabras separadas por puntos',
  INVALID_MONEY: 'El monto a ingresar no puede ser negativo',
  INVALID_EMPTY_MONEY: 'El monto a ingresar no puede ser cero',
  REQUIRED_FIELD: 'Campo requerido',
  MIN_LENGTH: 'Debe tener al menos 6 caracteres',
  MAX_LENGTH: 'Debe tener menos de 20 caracteres',
  MIN_LENGTH_NAME: 'Debe tener al menos 2 caracteres', // Fixed typo
  MIN_LENGTH_ALIAS: 'Debe tener al menos 10 caracteres', // Fixed typo
  MIN_LENGTH_MONEY: 'El mínimo para ingresar a la cuenta es de $100',
  INVALID_USER: 'El usuario ya existe',
  NOT_FOUND_USER: 'Usuario no encontrado',
  INVALID_CONFIRMATION_CODE: 'Codigo de verificacion o email invalidos',
  EMAIL_NOT_FOUND: 'No hemos encontrado ninguna cuenta asociada con ese correo electrónico. Asegúrate de que esté escrito correctamente.',
  INSUFFICIENT_FOUNDS: 'Fondos insuficientes.'
};

export enum SUCCESS_MESSAGES_KEYS {
  CARD_DELETED = 'CARD_DELETED',
  ALIAS_EDITED = 'ALIAS_EDITED',
  CARD_ADDED = 'CARD_ADDED',
  USER_REGISTER = 'USER_REGISTER',
  REGISTRATION_CONFIRMED = 'REGISTRATION_CONFIRMED',
  PASSWORD_RESET_EMAIL_SENT = 'PASSWORD_RESET_EMAIL_SENT',
  PASSWORD_RESET_SUCCESSFUL = 'PASSWORD_RESET_SUCCESSFUL',
}

export const SUCCESS_MESSAGES: Record<SUCCESS_MESSAGES_KEYS, string> = {
  CARD_DELETED: 'Tarjeta eliminada correctamente',
  ALIAS_EDITED: 'El alias se actualizó correctamente',
  CARD_ADDED: 'Tarjeta agregada correctamente',
  USER_REGISTER: 'Usuario registrado correctamente',
  REGISTRATION_CONFIRMED: '¡Registro confirmado! Gracias por unirte a nosotros.',
  PASSWORD_RESET_EMAIL_SENT: 'Se ha enviado un correo electrónico para restablecer tu contraseña.',
  PASSWORD_RESET_SUCCESSFUL: 'Tu contraseña ha sido restablecida exitosamente. Ahora puedes iniciar sesión con tu nueva contraseña.',
};

export const RECORD_MESSAGES: Record<ActivityType, string> = {
  [ActivityType.TRANSFER_IN]: 'Detalle',
  [ActivityType.TRANSFER_OUT]: 'Detalle',
  [ActivityType.DEPOSIT]: 'Detalle',
};


