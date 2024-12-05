import React, { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { CardCustom, Icon } from '../../components/';
import {
  currencies,
  RECORD_MESSAGES,
  ROUTES,
  UNAUTHORIZED,
} from '../../constants';
import { Button } from '@mui/material';
import {
  formatCurrency,
  printPage,
  calculateTransacionType,
  getUserActivity,
  downloadReceipt
} from '../../utils';
import { Transaction, ActivityType } from '../../types';
import { useAuth, useLocalStorage, useUserInfo } from '../../hooks';

// Asegúrate de importar la función downloadReceipt
// import { downloadReceipt } from '../../path/to/downloadReceipt';

const ActivityDetails = () => {
  const [userActivity, setUserActivity] = useState<Transaction | null>(null);
  const [activityType, setActivityType] = useState<ActivityType>(ActivityType.DEPOSIT);
  
  const [searchParams] = useSearchParams();
  const activityId = searchParams.get('id') || '1'; // Puedes dejarlo así, pero recuerda convertirlo a number
  const navigate = useNavigate();
  const { Argentina } = currencies;
  const { locales, currency } = Argentina;
  const [token] = useLocalStorage('token');
  const { logout } = useAuth();
  const { user } = useUserInfo();

  useEffect(() => {
    if (user && user.id) {
      getUserActivity(user.id, Number(activityId), token) // Convierte activityId a número
        .then((activity) => {
          if (activity && activity.amount && activity.type) {
            // Asegúrate de que 'date' sea una cadena en la actividad
            activity.date = formatDate(activity.date);
            setUserActivity(activity);
            setActivityType(calculateTransacionType(activity.amount, activity.type));
          }
        })
        .catch((error) => {
          if (error.status === UNAUTHORIZED) {
            logout();
          }
        });
    }
  }, [activityId, logout, token, user]);

  const formatDate = (dateString: string): string => {
    const date = new Date(dateString);

    if (isNaN(date.getTime())) {
      const timestamp = Number(dateString);
      if (!isNaN(timestamp)) {
        return new Date(timestamp).toLocaleString();
      }
      return 'Fecha no válida'; // Manejo de error en caso de fecha no válida
    }

    return date.toLocaleString(); // Personaliza el formato según tus necesidades
  };

  const handleDownloadReceipt = () => {
    if (user && user.id) {
      // Llama a la función downloadReceipt con los parámetros necesarios
      downloadReceipt(user.id, token, Number(activityId));
    }
  };

  return (
    <div>
      <CardCustom
        className="tw-flex tw-flex-col tw-justify-center tw-items-center tw-max-w-5xl print:tw-border-none print:tw-w-screen print:tw-h-screen print:!tw-mt-0"
        content={
          <div className="tw-border-neutral-blue-100 tw-rounded-lg print:tw-border-2 print:tw-p-48">
            <Icon
              className="tw-text-primary tw-mx-auto tw-mb-8 tw-hidden print:tw-block"
              type="digital-house"
            />
            <div className="tw-flex tw-flex-col tw-gap-y-6 tw-justify-center tw-mb-8 print:tw-mb-0">
              <div className="tw-flex tw-flex-col tw-gap-y-2 tw-items-center">
                <p>Monto</p>
                <p className="tw-text-xl tw-font-bold">
                  {userActivity && formatCurrency(locales, currency, Math.abs(userActivity.amount))}
                </p>
              </div>
              <div className="tw-flex tw-flex-col tw-gap-y-2 tw-items-center">
                <p>{RECORD_MESSAGES[activityType]}</p>
                <p className="tw-text-xl tw-font-bold">{userActivity?.name}</p>
                <p className="tw-text-xl tw-font-bold">{userActivity?.destination}</p>
                <p className="tw-font-bold" style={{ fontSize: '0.875rem' }}>{userActivity?.cvu}</p>
                <i>{userActivity ? formatDate(userActivity.date) : 'Fecha no disponible'}</i>
              </div>
            </div>
          </div>
        }
        actions={
          <div className="tw-flex tw-flex-col tw-justify-center tw-gap-y-8">
            <Button
              onClick={printPage}
              className="tw-h-12 tw-w-64 print:tw-hidden"
              variant="outlined"
            >
              Imprimir
            </Button>
            <Button
              onClick={handleDownloadReceipt} // Llama a la función de descarga
              className="tw-h-12 tw-w-64 print:tw-hidden"
              variant="contained"
            >
              Descargar Comprobante
            </Button>
            <Button
              onClick={() => navigate(ROUTES.HOME)}
              className="tw-h-12 tw-w-64 print:tw-hidden"
              variant="contained"
            >
              Continuar
            </Button>
          </div>
        }
      />
    </div>
  );
};

export default ActivityDetails;
