import { MatTableDataSource } from '@angular/material/table';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MatSort } from '@angular/material';
import { PaymentIndependentService } from '@core/services/payment-independent/payment-independent.service';

@Component({
  selector: 'app-dialog-search-field',
  templateUrl: './dialog-search-field.component.html',
  styleUrls: ['./dialog-search-field.component.scss'],
})
export class DialogSearchFieldComponent implements OnInit, AfterViewInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  searchFiledFormCreate: FormGroup;

  typeControl: FormControl; // ชื่อฟิลด์

  errorMessage = '';

  displayedColumns: string[] = ['choose', 'fieldName', 'dbName'];

  dataSource = new MatTableDataSource([]);

  constructor(
    private dialogRef: MatDialogRef<DialogSearchFieldComponent>,
    private formBuilder: FormBuilder,
    private paymentIndependentService: PaymentIndependentService
  ) {}

  ngOnInit() {
    this.createSearchFiledFormControl();
    this.createSearchFiledFormGroup();
  }

  ngAfterViewInit() {
    this.searchStandard();
  }

  createSearchFiledFormControl() {
    this.typeControl = this.formBuilder.control('1');
  }

  createSearchFiledFormGroup() {
    this.searchFiledFormCreate = this.formBuilder.group({
      type: this.typeControl,
    });
  }

  closeDialog(): void {
    const dialogRef = this.dialogRef.close();
  }

  chooseDataSearch(data) {
    this.dialogRef.close({
      event: true,
      fieldName: data.fieldName,
      dataType: data.dataType,
      dbName: data.dbName,
      tableName: data.tableName,
    });
  }

  search(groupName) {
    this.paymentIndependentService.search(groupName).then((value) => {
      this.dataSource = new MatTableDataSource(value.data);
      this.dataSource.sort = this.sort;
    });
  }

  searchStandard() {
    this.paymentIndependentService.searchStandard().then((value) => {
      this.dataSource = new MatTableDataSource(value.data);
      this.dataSource.sort = this.sort;
    });
  }

  changeType(event) {
    if (event.value === '1') {
      this.searchStandard();
    } else if (event.value === '2') {
      this.search('Document');
    } else if (event.value === '3') {
      this.search('Vendor');
    }
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}
