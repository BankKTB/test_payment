import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { PaymentIndependentService } from '@core/services/payment-independent/payment-independent.service';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { DialogConfirmDeleteMasterComponent } from '@shared/component/dialog-confirm-delete-master/dialog-confirm-delete-master.component';

export interface DialogData {
  role: string;
  createBy: string;
}

@Component({
  selector: 'app-dialog-om-search-column-table',
  templateUrl: './dialog-om-search-column-table.component.html',
  styleUrls: ['./dialog-om-search-column-table.component.scss'],
})
export class DialogOmSearchColumnTableComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild('searchValue', { static: true }) searchValue: ElementRef;

  searchFiledFormCreate: FormGroup;

  typeControl: FormControl; // ชื่อฟิลด์

  errorMessage = '';

  displayedColumns: string[] = ['choose', 'name', 'createdBy', 'created', 'delete'];

  dataSource = new MatTableDataSource([]);
  isLoading = false;
  button = 'Submit';

  constructor(
    private dialogRef: MatDialogRef<DialogOmSearchColumnTableComponent>,
    private formBuilder: FormBuilder,
    private paymentIndependentService: PaymentIndependentService,
    private paymentBlockService: PaymentBlockService,
    private dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {}

  ngOnInit() {
    this.createSearchFiledFormControl();
    this.createSearchFiledFormGroup();
  }

  click() {
    this.isLoading = true;
    this.button = 'Processing';

    setTimeout(() => {
      this.isLoading = false;
      this.button = 'Submit';
    }, 2400);
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
      value: data,
    });
  }

  search(e) {
    let value = '';
    if (!e.value) {
      value = '**';
    } else {
      value = e.value;
    }

    value = this.checkStarAndPercent(value);
    if (value === 'error') {
      return;
    }

    this.paymentBlockService
      .searchColumnAllByRole(this.data.role, this.data.createBy, value)
      .then((result) => {
        console.log(result);
        if (result.status === 200) {
          this.dataSource = new MatTableDataSource(result.data);
          this.dataSource.sort = this.sort;
        } else if (result.status === 404) {
          this.dataSource = new MatTableDataSource([]);
        }
      });
  }

  searchStandard() {
    this.paymentBlockService
      .searchColumnAllByRole(this.data.role, this.data.createBy, '**')
      .then((result) => {
        console.log(result);
        if (result.status === 200) {
          this.dataSource = new MatTableDataSource(result.data);
          this.dataSource.sort = this.sort;
        } else if (result.status === 404) {
          this.dataSource = new MatTableDataSource([]);
        }
      });
  }

  deleteInput(element) {
    const dialogRef = this.dialog.open(DialogConfirmDeleteMasterComponent, {
      data: { value: null },
      disableClose: true,
    });
    dialogRef.afterClosed().subscribe((response) => {
      console.log(response);
      console.log(this.searchValue.nativeElement.value);
      if (response) {
        if (response.event && response.value === 'Delete') {
          const id = element.id;
          this.paymentBlockService.deleteColumnById(id).then((result) => {
            console.log(result);
            if (result.status === 200) {
              const data = result.data;
              if (data) {
                if (this.searchValue.nativeElement.value) {
                  this.search(this.searchValue.nativeElement);
                } else {
                  this.searchStandard();
                }
              }
            } else if (result.status === 404) {
              this.dataSource = new MatTableDataSource([]);
            }
          });
        }
      }
    });
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  checkStarAndPercent(textSearch) {
    console.log(textSearch);
    const slash = textSearch.split('').filter((value) => value === '/').length;
    if (slash > 0) {
      for (let i = 0; i < slash; i++) {
        textSearch = textSearch.replace('/', '');
      }
    }
    console.log(textSearch);

    const percent = textSearch.split('').filter((value) => value === '%').length;
    if (percent > 0) {
      for (let i = 0; i < percent; i++) {
        textSearch = textSearch.replace('%', '*');
      }
      const textStar = textSearch.split('').filter((value) => value === '*').length;
      if (textStar > 2) {
        this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
        return 'error';
      } else {
        const checkCondtion = textSearch.indexOf('**');
        if (checkCondtion === -1) {
          if (textSearch === '*' || textSearch === '**') {
            this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
            return 'error';
          }
        } else {
          this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
          return 'error';
        }
      }
    } else {
      const textStar = textSearch.split('').filter((value) => value === '*').length;
      if (textStar === 0) {
        textSearch = '*' + textSearch + '*';
      }
    }
    return textSearch;
  }
}
