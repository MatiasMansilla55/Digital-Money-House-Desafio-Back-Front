import React, { useEffect, useState } from 'react';
import { RecordVariant } from '../../types/';
import {
  CardCustom,
  Records,
  Skeleton,
  SkeletonVariant,
} from '../../components';
import Pagination from '@mui/material/Pagination';
import { usePagination } from '../../hooks/usePagination';
import { ROUTES, UNAUTHORIZED } from '../../constants';
import PaginationItem from '@mui/material/PaginationItem';
import { Link } from 'react-router-dom';
import {
  getUserActivities,
  pageQuery,
  sortByDate,
} from '../../utils';
import { Transaction } from '../../types';
import { useUserInfo, useLocalStorage, useAuth } from '../../hooks';

const recordsPerPage = 10;

const Activity = () => {
  const [userActivities, setUserActivities] = useState<any[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [token] = useLocalStorage('token');
  const { pageNumber, numberOfPages, isRecordsGreeterThanOnePage } =
    usePagination(userActivities, recordsPerPage);
  const { logout } = useAuth();
  const { user } = useUserInfo();

  useEffect(() => {
    if (user && user.id) {
      getUserActivities(user.id, token)
        .then((activities) => {
          if (activities.length > 0) {
            const orderedActivities = sortByDate(activities);
            const parsedRecords = orderedActivities.map(
              (parsedActivity: Transaction) => ({
                content: parsedActivity,
                variant: RecordVariant.TRANSACTION,
              })
            );
            setUserActivities(parsedRecords);
          }
        })
        .catch((error) => {
          if (error.status === UNAUTHORIZED) {
            logout();
          } else {
            console.error("Error fetching activities:", error);
          }
        })
        .finally(() => setIsLoading(false));
    }
  }, [logout, token, user]);

  return (
    <div className="tw-w-full">
      <CardCustom
        className="tw-max-w-5xl"
        content={
          <>
            <div>
              <p className="tw-mb-4 tw-font-bold">Tu actividad</p>
            </div>
            {userActivities.length > 0 && !isLoading && (
              <Records
                records={userActivities}
                initialRecord={pageNumber * recordsPerPage - recordsPerPage}
                maxRecords={recordsPerPage * pageNumber}
              />
            )}
            {userActivities.length === 0 && !isLoading && (
              <p>No hay actividad registrada</p>
            )}
            {isLoading && <Skeleton variant={SkeletonVariant.RECORD_LIST} />}
          </>
        }
        actions={
          isRecordsGreeterThanOnePage && (
            <div className="tw-h-12 tw-w-full tw-flex tw-items-center tw-justify-center tw-px-4 tw-mt-4">
              <Pagination
                count={numberOfPages}
                shape="rounded"
                renderItem={(item) => (
                  <PaginationItem
                    component={Link}
                    to={pageQuery(ROUTES.ACTIVITY, item.page as number)}
                    {...item}
                  />
                )}
              />
            </div>
          )
        }
      />
    </div>
  );
};

export default Activity;
