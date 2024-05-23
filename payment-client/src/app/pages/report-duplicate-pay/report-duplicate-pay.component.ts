import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Utils } from '@shared/utils/utils';
import { DialogCriteriaComponent } from '@shared/component/dialog-criteria/dialog-criteria.component';
import { MatDialog } from '@angular/material/dialog';
import { ReportService } from '@core/services/report/report-service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { SidebarService } from '@core/services';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';

interface DuplicatePayment {
  originalCompCode: string;
  originalDocNo: string;
  originalFiscalYear: string;
  paymentDocument: string;
  amount: string;
  paymentName: string;
  paymentDate: Date;
}

@Component({
  selector: 'app-report-duplicate-pay',
  templateUrl: './report-duplicate-pay.component.html',
  styleUrls: ['./report-duplicate-pay.component.scss'],
})
export class ReportDuplicatePayComponent implements OnInit {
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  formGroup: FormGroup;
  isSearch: boolean = true;
  listPaymentDate = [{ from: null, to: null, optionExclude: false }];
  listValidate = [];
  displayedColumns: string[] = [
    'originalCompCode',
    'originalDocNo',
    'originalFiscalYear',
    'paymentDocument',
    'amount',
    'paymentName',
    'paymentDate',
  ];
  dataSource = new MatTableDataSource<DuplicatePayment>();
  currentDate = new Date();
  mapValidate = new Map<string, string>();

  constructor(
    private formBuilder: FormBuilder,
    public utils: Utils,
    private sidebarService: SidebarService,
    private dialog: MatDialog,
    private reportService: ReportService
  ) {
    this.createFormGroup();
    this.initMapValidateField();
  }

  ngOnInit() {
    this.sidebarService.updatePageType('config');
    this.sidebarService.updateNowPage('smartFee');
  }

  validateForm() {
    Object.keys(this.formGroup.controls).forEach((key) => {
      const controlErrors = this.formGroup.get(key).errors;
      if (controlErrors) {
        this.listValidate.push(this.mapValidate.get(key));
      }
    });
  }

  initMapValidateField() {
    this.mapValidate.set('paymentDateFrom', 'กรุณาระบุ วันที่จ่าย');
  }

  createFormGroup() {
    const date = new Date();
    this.formGroup = this.formBuilder.group({
      companyCode: this.formBuilder.control(''),
      paymentDateFrom: this.formBuilder.control(new Date(date.setDate(new Date().getDate() - 7)), [
        Validators.required,
      ]),
      paymentDateTo: this.formBuilder.control(new Date()),
    });
  }

  onSearch() {
    const form = this.formGroup.getRawValue();
    this.listValidate = [];
    this.validateForm();
    if (this.listValidate.length <= 0) {
      let paymentDateFrom = '';
      if (form.paymentDateFrom) {
        const dayPaymentDateFrom = form.paymentDateFrom.getDate();
        const monthPaymentDateFrom = form.paymentDateFrom.getMonth() + 1;
        const yearPaymentDateFrom = form.paymentDateFrom.getFullYear();
        paymentDateFrom = this.utils.parseDate(
          dayPaymentDateFrom,
          monthPaymentDateFrom,
          yearPaymentDateFrom
        );
        this.listPaymentDate[0].from = paymentDateFrom;
      }
      let paymentDateTo = '';
      if (form.paymentDateTo) {
        const dayPaymentDateTo = form.paymentDateTo.getDate();
        const monthPaymentDateTo = form.paymentDateTo.getMonth() + 1;
        const yearPaymentDateTo = form.paymentDateTo.getFullYear();
        paymentDateTo = this.utils.parseDate(
          dayPaymentDateTo,
          monthPaymentDateTo,
          yearPaymentDateTo
        );
        this.listPaymentDate[0].to = paymentDateTo;
      }
      this.isSearch = false;
      const request = {
        companyCode: form.companyCode,
        paymentDate: this.listPaymentDate,
      };
      this.reportService.reportDuplicatePayment(request).then((e) => {
        this.dataSource = new MatTableDataSource<DuplicatePayment>([]);
        if (e.status === 200) {
          if (e.data.length > 0) {
            this.dataSource = new MatTableDataSource<DuplicatePayment>(e.data);
            if (this.dataSource.data) {
              this.dataSource.paginator = this.paginator;
            }
          }
        } else if (e.status === 404) {
          this.listValidate.push('ไม่พบข้อมูล');
        } else {
          //TODO handle error
        }
      });
    }
  }

  back() {
    this.dataSource = new MatTableDataSource<DuplicatePayment>([]);
    this.isSearch = true;
  }

  openDialogDateParameter(keyInput: string) {
    let listCriteria = [{ from: null, to: null, optionExclude: false }];
    let thaiText = 'ค้นหา';
    let typeInput = 'STRING';
    if (keyInput === 'paymentDate') {
      listCriteria = this.listPaymentDate;
      thaiText = 'วันที่ประมวลผล';
      typeInput = 'DATE';
      const dialogRef = this.dialog.open(DialogCriteriaComponent, {
        width: '60vw',
        data: {
          listCriteria,
          typeParam: 'LIST',
          keyColumn: 'key',
          typeInput,
          thaiText,
          enableExclude: true,
        },
      });
      dialogRef.afterClosed().subscribe((result) => {
        if (result && result.event) {
          if (result.status === 'save') {
            const values = result.value;
            if (keyInput === 'paymentDate') {
              this.listPaymentDate = values;
              if (values.length > 0) {
                this.formGroup.patchValue({
                  paymentDateFrom: values[0].from ? new Date(values[0].from) : '',
                  paymentDateTo: values[0].to ? new Date(values[0].to) : '',
                });
              }
            }
          }
        }
      });
    }
  }

  openDialogSearchMaster(type): void {
    const dialogRef = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        this.formGroup.patchValue({
          companyCode: result.value,
        });
      }
    });
  }
}
