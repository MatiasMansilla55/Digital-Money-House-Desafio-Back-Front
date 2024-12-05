export interface User {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phone?: string;
  dni?: string;
  id?: number;
}

export interface UserAccount {
  balance: number;
  cvu: string;
  alias: string;
  userId: number;
  id: number;
  name: string;
}
