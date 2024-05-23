import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ProposalTr1Service } from '@core/services/proposal-tr1/proposal-tr1.service';
import { Utils } from '@shared/utils/utils';
import { DialogCriteriaComponent } from '@shared/component/dialog-criteria/dialog-criteria.component';
import { LocalStorageService, SidebarService } from '@core/services';
import { UserProfile } from '@core/models/user-profile';

@Component({
  selector: 'app-proposal-tr1',
  templateUrl: './proposal-tr1.component.html',
  styleUrls: ['./proposal-tr1.component.scss'],
})
export class ProposalTr1Component implements OnInit {
  formGroup: FormGroup;
  isSearch: boolean = true;
  result: any[];
  rawResult: any[];
  listValidate = [];
  mapValidate = new Map<string, string>();
  listRefNumber = [{ from: null, to: null, optionExclude: false }];
  listPaymentDate = [{ from: null, to: null, optionExclude: false }];
  listPaymentName = [{ from: null, to: null, optionExclude: false }];
  userProfile: UserProfile;
  pageOption: any = {
    rowTotal: 0,
    current: 1,
    size: 100,
  };

  constructor(
    public utils: Utils,
    private formBuilder: FormBuilder,
    private proposalTr1Service: ProposalTr1Service,
    private dialog: MatDialog,
    private sidebarService: SidebarService,
    private localStorageService: LocalStorageService
  ) {
    this.createFormGroup();
    this.initMapValidateField();
  }

  ngOnInit() {
    this.userProfile = this.localStorageService.getUserProfile();
    this.sidebarService.updatePageType('generate');
    this.sidebarService.updateNowPage('proposal-tr1');
  }

  createFormGroup() {
    this.formGroup = this.formBuilder.group({
      period: this.formBuilder.control('', [Validators.required]),
      fiscalYear: this.formBuilder.control(this.utils.CalculateFiscYear(new Date()), [
        Validators.required,
      ]),
      refNumberFrom: this.formBuilder.control(''),
      refNumberTo: this.formBuilder.control(''),
      paymentDateFrom: this.formBuilder.control(''),
      paymentDateTo: this.formBuilder.control(''),
      paymentNameFrom: this.formBuilder.control(''),
      paymentNameTo: this.formBuilder.control(''),
      updateTablePropLogTr1: this.formBuilder.control(false),
    });
  }

  initMapValidateField() {
    this.mapValidate.set('period', 'กรุณาระบุ Period');
    this.mapValidate.set('fiscalYear', 'กรุณาระบุ ปีบัญชี');
  }

  validateForm() {
    Object.keys(this.formGroup.controls).forEach((key) => {
      const controlErrors = this.formGroup.get(key).errors;
      if (controlErrors) {
        this.listValidate.push(this.mapValidate.get(key));
      }
    });
  }

  initialSearch() {
    this.pageOption = {
      rowTotal: 0,
      current: 1,
      size: 100,
    };
    this.rawResult = [];
    this.result = [];
  }

  onSearch() {
    const form = this.formGroup.getRawValue();
    this.listValidate = [];
    this.validateForm();
    if (this.listValidate.length <= 0) {
      this.listRefNumber[0] = {
        ...this.listRefNumber[0],
        from: form.refNumberFrom,
        to: form.refNumberTo,
      };
      this.listPaymentDate[0].from = form.paymentDateFrom;
      this.listPaymentDate[0].to = form.paymentDateTo;
      let paymentDateFrom = '';
      if (form.paymentDateFrom) {
        const daypaymentDateFrom = form.paymentDateFrom.getDate();
        const monthpaymentDateFrom = form.paymentDateFrom.getMonth() + 1;
        const yearpaymentDateFrom = form.paymentDateFrom.getFullYear();
        paymentDateFrom = this.utils.parseDate(
          daypaymentDateFrom,
          monthpaymentDateFrom,
          yearpaymentDateFrom
        );
        this.listPaymentDate[0].from = paymentDateFrom;
      }
      let paymentDateTo = '';
      if (form.paymentDateTo) {
        const daypaymentDateTo = form.paymentDateTo.getDate();
        const monthpaymentDateTo = form.paymentDateTo.getMonth() + 1;
        const yearpaymentDateTo = form.paymentDateTo.getFullYear();
        paymentDateTo = this.utils.parseDate(
          daypaymentDateTo,
          monthpaymentDateTo,
          yearpaymentDateTo
        );
        this.listPaymentDate[0].to = paymentDateTo;
      }
      this.listPaymentName[0] = {
        ...this.listPaymentName[0],
        from: form.paymentNameFrom,
        to: form.paymentNameTo,
      };
      this.isSearch = false;
      const request = {
        period: form.period,
        fiscalYear: this.utils.convertYearToAD(form.fiscalYear),
        refNumber: this.listRefNumber,
        paymentDate: this.listPaymentDate,
        paymentName: this.listPaymentName,
        updateTablePropLogTr1: form.updateTablePropLogTr1,
        username: this.userProfile.userdata.username,
      };
      this.initialSearch();
      this.proposalTr1Service.getSummaryTR1(request).then((r) => {
        this.pageOption = {
          ...this.pageOption,
          rowTotal: r.data.length || 0,
        };
        this.rawResult = [...r.data];
        console.log(r);
        this.result = r &&
          r.data && [...this.paginate(r.data, this.pageOption.size, this.pageOption.current)];
        // this.result = r.data
      });
    }
  }

  back() {
    this.isSearch = true;
    this.result = [];
    this.initialSearch();
  }

  findSummaryGroup(refRunning: string): number {
    const obj = this.result.find((e) => e.refRunning === refRunning && e.rowLevel === 'LEVEL1');
    if (obj) {
      return obj.amount;
    }
    return 0;
  }

  openDialogDateParameter(keyInput: string) {
    let listCriteria = [{ from: null, to: null, optionExclude: false }];
    let thaiText = 'ค้นหา';
    let typeInput = 'STRING';
    if (keyInput === 'refNumber') {
      this.listRefNumber[0] = {
        from: this.formGroup.controls.refNumberFrom.value,
        to: this.formGroup.controls.refNumberTo.value,
        optionExclude: this.listRefNumber.length > 0 ? this.listRefNumber[0].optionExclude : false,
      };
      listCriteria = this.listRefNumber;
      typeInput = 'NUMBER';
      thaiText = 'Ref number index';
    } else if (keyInput === 'paymentDate') {
      this.listPaymentDate[0] = {
        from: this.formGroup.controls.paymentDateFrom.value,
        to: this.formGroup.controls.paymentDateTo.value,
        optionExclude:
          this.listPaymentDate.length > 0 ? this.listPaymentDate[0].optionExclude : false,
      };
      listCriteria = this.listPaymentDate;
      thaiText = 'วันที่ประมวลผล';
      typeInput = 'DATE';
    } else if (keyInput === 'paymentName') {
      this.listPaymentName[0] = {
        from: this.formGroup.controls.paymentNameFrom.value,
        to: this.formGroup.controls.paymentNameTo.value,
        optionExclude:
          this.listPaymentName.length > 0 ? this.listPaymentName[0].optionExclude : false,
      };
      listCriteria = this.listPaymentName;
      thaiText = 'การกำหนด';
      typeInput = 'STRING';
    }
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
          if (keyInput === 'refNumber') {
            this.listRefNumber = values;
            if (values.length > 0) {
              this.formGroup.patchValue({
                refNumberFrom: values.length > 0 ? (values[0].from ? values[0].from : '') : '',
                refNumberTo: values.length > 0 ? (values[0].to ? values[0].to : '') : '',
              });
            }
          } else if (keyInput === 'paymentDate') {
            this.listPaymentDate = values;
            this.formGroup.patchValue({
              paymentDateFrom:
                values.length > 0 ? (values[0].from ? new Date(values[0].from) : '') : '',
              paymentDateTo: values.length > 0 ? (values[0].to ? new Date(values[0].to) : '') : '',
            });
          } else if (keyInput === 'paymentName') {
            this.listPaymentName = values;
            if (values.length > 0) {
              this.formGroup.patchValue({
                paymentNameFrom: values.length > 0 ? (values[0].from ? values[0].from : '') : '',
                paymentNameTo: values.length > 0 ? (values[0].to ? values[0].to : '') : '',
              });
            }
          }
        }
      }
    });
  }

  paginate(array: any[], pageSize: number, pageNumber: number) {
    return array.slice((pageNumber - 1) * pageSize, pageNumber * pageSize);
  }

  pageChange(e: any) {
    this.pageOption = {
      ...this.pageOption,
      current: e.pageIndex + 1,
      size: e.pageSize,
    };
    this.result = [...this.paginate(this.rawResult, e.pageSize, e.pageIndex + 1)];
  }
}
