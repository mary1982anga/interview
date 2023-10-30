import { Component , OnInit} from '@angular/core';
import { ProductService } from 'src/app/service/product.service';
import { Product } from '../../model/Product';



@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  products: any[] = [];
  categories: string[] = [];

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.products = this.productService.getProducts();
    this.extractCategories();
  }

  private extractCategories() {
    this.products.forEach(product => {
      if (!this.categories.includes(product.category)) {
        this.categories.push(product.category);
      }
    });
  }

  filterByCategory(event: any) {
    const selectedCategory = event?.target?.value;
    
    if (selectedCategory === 'All') {
      this.products = this.productService.getProducts();
    } else {
      this.products = this.productService.getProducts().filter(product => product.category === selectedCategory);
    }
  }

  isSpecial(product: any): boolean {
    return product.isSpecial;
  }

  deleteProduct(index: number) {
    this.products.splice(index, 1);
  }
}