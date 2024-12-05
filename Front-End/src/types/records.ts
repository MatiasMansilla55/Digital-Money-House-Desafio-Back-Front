export enum TransactionType {
  Transfer = 'transfer-out',
  Deposit = 'Deposit',
}

export interface Transaction {
  amount: number;
  name?: string;
  date: string;
  id: number;
  type: TransactionType;
  origin?: string;
  destination?: string;
  cvu?: string;
}

export interface Card {
  number: string;
  name: string;
  type: string;
  id: string;
}

export interface Account {
  name: string;
  origin: string;
  cvu: string
}

export enum ActivityType {
  TRANSFER_IN = 'transfer-in',
  TRANSFER_OUT = 'transfer-out',
  DEPOSIT = 'deposit',
}

// Nuevo Enum para RecordVariant
export enum RecordVariant {
  TRANSACTION = 'transaction',
  // Puedes agregar más variantes aquí si es necesario
}
