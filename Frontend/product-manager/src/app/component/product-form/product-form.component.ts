import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { ProductService } from '../../service/product.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.css']
})
export class ProductFormComponent implements OnInit {
  productForm: FormGroup;

  isEditMode: boolean = false;

  constructor(
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.productForm = this.fb.group({
      description: ['', Validators.required],
      canExpire: [false],
      expiryDate: [null],
      category: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(0)]],
      isSpecial: [false]
    });
  }

  ngOnInit() {
    const productId = this.route.snapshot.params['id'];

    if (productId) {
      const existingProduct = this.productService.getProduct(productId);

      if (existingProduct) {
        this.productForm.setValue(existingProduct);
        this.isEditMode = true;
      } else {
        alert('Product not found');
        this.router.navigate(['/product-list']);
      }
    }

    const canExpireControl = this.productForm.get('canExpire');

    if (canExpireControl?.value) {
      // Do something with canExpireControl.value
      const expiryDate = this.productForm.get('expiryDate')?.value;
    
      // Assuming you want to perform some action with the expiryDate
      if (expiryDate) {
        console.log(`The product will expire on: ${expiryDate}`);
      }
    }

  }

  onSubmit() {
    if (this.productForm.valid) {
      const product = this.productForm.value;
      const success = this.productService.addOrUpdateProduct(product);

      if (success) {
        this.router.navigate(['/product-list']);
      } else {
        alert('Error occurred while adding/updating the product.');
      }
    } else {
      alert('Please fill in all required fields.');
    }
  }

  clearForm() {
    this.productForm.reset(); 
  }
}