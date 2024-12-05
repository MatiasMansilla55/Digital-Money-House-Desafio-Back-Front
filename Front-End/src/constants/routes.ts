export const ROUTES = {
  HOME: '/',
  LOGIN: '/login',
  REGISTER: '/register',
  CONFIRM_REGISTRATION: '/confirm-registration',
  RECOVER_PASSWORD: '/recover-password',
  RESET_PASSWORD: '/reset-password',
  PROFILE: '/profile',
  ACTIVITY: '/activities',
  ACTIVITY_DETAILS: '/activity',
  LOAD_MONEY: '/load-money',
  SEND_MONEY: '/send-money',
  CARDS: '/cards',
  NOT_FOUND: '/404',
  DASHBOARD: '/dashboard'
};

export const LINK_LIST = [
  {
    name: 'Inicio',
    href: ROUTES.DASHBOARD,
  },
  {
    name: 'Actividad',
    href: ROUTES.ACTIVITY,
  },
  {
    name: 'Tu perfil',
    href: ROUTES.PROFILE,
  },
  {
    name: 'Cargar Dinero',
    href: ROUTES.LOAD_MONEY,
  },
  {
    name: 'Enviar Dinero',
    href: ROUTES.SEND_MONEY,
  },
  {
    name: 'Mis Tarjetas',
    href: ROUTES.CARDS,
  },
  {
    name: 'Cerrar Sesión',
    href: ROUTES.LOGIN,
  }
];