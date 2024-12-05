import React from 'react';
import { RecordProps, Record, IRecord } from './components/record';
import { Transaction } from '../../types'

export interface RecordsProps {
  records: RecordProps[];
  maxRecords?: number;
  initialRecord?: number;
  setRecords?:React.Dispatch<React.SetStateAction<IRecord[]>>;
}

export const Records = ({
  records,
  maxRecords,
  initialRecord = 0,
  setRecords
}: RecordsProps) => {
  const recordsToShow = records.slice(initialRecord, maxRecords);
  return (
    <ul className="tw-w-full">
      {recordsToShow &&
        recordsToShow.map((record: RecordProps, index: number) => (
          <li
            key={`record-${index}`}
            className={`
              tw-p-4 tw-border tw-rounded-lg tw-bg-dark-blue tw-shadow-md
              ${index + 1 === recordsToShow.length && 'tw-border-b'}`}
          >
            {/* Verificación de la propiedad cvu */}
            {'cvu' in record.content ? (
              <p className="tw-font-semibold">
                CVU: {(record.content as Transaction).cvu}
              </p>
            ) : (
              <p className="tw-text-gray-500">No CVU available</p>
            )}

            {/* Renderizado del resto del contenido del registro */}
            <Record
              {...record}
              className={`
                ${index + 1 === recordsToShow.length && 'tw-border-b'}`}
              setRecords={setRecords}
            />
          </li>
        ))}
    </ul>
  );
};


export * from './components/record';
