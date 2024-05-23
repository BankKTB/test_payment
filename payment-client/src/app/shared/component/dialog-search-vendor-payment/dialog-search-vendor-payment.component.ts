import { MatTableDataSource } from '@angular/material/table';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSort } from '@angular/material';
import { MasterService } from '@core/services';

@Component({
  selector: 'app-dialog-search-vendor-payment',
  templateUrl: './dialog-search-vendor-payment.component.html',
  styleUrls: ['./dialog-search-vendor-payment.component.scss'],
})
export class DialogSearchVendorPaymentComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  displayedColumns: string[] = ['choose', 'taxId', 'valueCode', 'name'];
  dataSource = new MatTableDataSource([]);
  errorMessage = '';

  constructor(
    private dialogRef: MatDialogRef<DialogSearchVendorPaymentComponent>,
    private masterService: MasterService
  ) {}

  closeDialog(): void {
    const dialogRef = this.dialogRef.close();
  }

  ngOnInit() {
    // this.loadVendor('')
  }

  loadVendor(event) {
    // console.log(textSearch.value)

    // let textSearch = event.value

    // const percent = textSearch.split('').filter(value => value === '%').length;
    // for (let i = 0; i < percent; i++) {
    //   textSearch = textSearch.replace('%', '*');
    // }
    // const text = textSearch.split('').filter(value => value === '*').length;
    // if (text > 2) {
    //   this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
    //   return false;
    // } else {
    //   const checkCondtion = textSearch.indexOf('**');

    //   if (checkCondtion === -1) {
    //     if (textSearch === '*' || textSearch === '**') {
    //       this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
    //       return false;
    //     }
    //   } else {
    //     this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
    //     return false;
    //   }
    // }
    // this.errorMessage = '';
    // this.dataSource = []

    // this.masterService.findVendorCodeWithParam(textSearch).subscribe(data => {
    //   const response = data as any;

    //   if (response.status === 'T') {
    //     this.dataSource = response.data;
    //     console.log(JSON.stringify(response.data))
    //     // this.dataSourceHeader = this.constant.HEADER_DIALOG_SEARCH.vendorTaxId;
    //   } else {
    //     this.errorMessage = response.message;
    //   }
    // });

    this.dataSource.sort = this.sort;
  }

  chooseDataSearch(data) {
    console.log(data);
    // this.errorMessage = '';
    this.dialogRef.close({
      event: true,
      // type: this.data.type,
      value: data.valueCode,
    });
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}
