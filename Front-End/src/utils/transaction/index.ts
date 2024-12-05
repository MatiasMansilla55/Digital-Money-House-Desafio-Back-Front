import { ActivityType, TransactionType } from '../../types';

export const calculateTransacionType = (
  amount: number,
  type: string,
): ActivityType => {
  const isPositive = amount > 0;
  if (type === TransactionType.Transfer) {
    return isPositive ? ActivityType.TRANSFER_IN : ActivityType.TRANSFER_OUT;
  }
  return ActivityType.DEPOSIT;
};

