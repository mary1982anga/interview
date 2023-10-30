import { Injectable } from '@angular/core';
import { Product } from '../model/Product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  
    private products: Product[] = [];
  
    constructor() {
      // Initialize some sample products (you can replace this with your own logic)
      this.products.push(
        { id: '1', description: 'Product 1', canExpire: false, expiryDate: undefined, category: 'Category 1', price: 10, isSpecial: false },
        { id: '2', description: 'Product 2', canExpire: true, expiryDate: new Date('2023-12-31'), category: 'Category 2', price: 20, isSpecial: true }
      );
    }
  
    getProducts(): Product[] {
      return this.products;
    }
  
    getProduct(id: string): Product | undefined {
      return this.products.find(product => product.id === id);
    }
  
    addOrUpdateProduct(product: Product): boolean {
      if (!product.id) {
        // Generate a unique ID (you may use a more sophisticated method)
        product.id = this.generateUniqueId();
        this.products.push(product);
      } else {
        const existingProductIndex = this.products.findIndex(p => p.id === product.id);
        if (existingProductIndex !== -1) {
          this.products[existingProductIndex] = product;
        } else {
          return false; // Product with given ID not found
        }
      }
      return true;
    }
  
    deleteProduct(id: string): boolean {
      const index = this.products.findIndex(product => product.id === id);
      if (index !== -1) {
        this.products.splice(index, 1);
        return true;
      }
      return false; // Product with given ID not found
    }
  
    private generateUniqueId(): string {
      // This is a very simple method for demonstration purposes
      return Math.random().toString(36).substring(2);
    }
  }