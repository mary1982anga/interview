export interface Product {
  id: string;
  description: string;
  canExpire: boolean;
  expiryDate: Date | undefined;
  category: string;
  price: number;
  isSpecial: boolean;
}