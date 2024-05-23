import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Utils } from '@shared/utils/utils';
import { Constant } from '@shared/utils/constant';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { LoaderService, SidebarService } from '@core/services';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { DialogCopyParameterComponent } from '@shared/component/dialog-copy-parameter/dialog-copy-parameter.component';
import { UserProfile } from '@core/models/user-profile';

@Component({
  selector: 'app-proposal-detail',
  templateUrl: './proposal-detail.component.html',
  styleUrls: ['./proposal-detail.component.scss'],
})
export class ProposalDetailComponent implements OnInit {
  button = 'Submit';

  panelExpanded = true;
  panelExpanded1 = true;
  panelExpanded2 = true;

  omForm: FormGroup;

  isOpenCollapseDetail = true; // เปิดปิด collpase

  isSubmitedForm = false;
  listValidate = [];
  listSearchValidate = [];

  role = 'USER_CGD';

  isDisableStatus = true;

  currentDate = new Date();

  report = null;
  userProfile: UserProfile = null;
  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    public utils: Utils,
    private constant: Constant,
    private paymentBlockService: PaymentBlockService,
    private sidebarService: SidebarService,
    private loaderService: LoaderService
  ) {}

  ngOnInit() {
    this.currentDate.setHours(0);
    this.currentDate.setMinutes(0);
    this.currentDate.setSeconds(0);
    this.currentDate.setMilliseconds(0);

    this.sidebarService.updatePageType('om');
    this.sidebarService.updateNowPage('om-result-e');
    this.utils.listYear = this.utils.CalculateYear();
    this.utils.fiscYear = this.utils.CalculateFiscYear(new Date());

    this.createFormGroup();
  }

  createFormGroup() {
    this.omForm = this.formBuilder.group({
      paymentDate: this.formBuilder.control(''),
      paymentName: this.formBuilder.control(''),

      departmentPayFrom: this.formBuilder.control(''),
      departmentPayTo: this.formBuilder.control(''),
      departmentSendFrom: this.formBuilder.control(''),
      departmentSendTo: this.formBuilder.control(''),
      provinceCodeFrom: this.formBuilder.control(''),

      statusDocumentFrom: this.formBuilder.control(''),
      statusDocumentTo: this.formBuilder.control(''),

      keyBlockPaymentFrom: this.formBuilder.control(''),
      keyBlockPaymentTo: this.formBuilder.control(''),
    });
  }

  onSearch() {
    const form = this.omForm.value;

    this.listSearchValidate = [];
    this.report = null;

    // if (!form.departmentCodeFrom) {
    //   this.listSearchValidate.push('กรุณา กรอก รหัสหน่วยงาน');
    // }
    // if (!form.unblockDate) {
    //   this.listSearchValidate.push('กรุณา เลือก วันที่อนุมัติ');
    // }

    this.isSubmitedForm = true;
    if (this.listSearchValidate.length <= 0) {
      this.isSubmitedForm = false;

      const payload = {
        compCodeFrom: '',
        compCodeTo: '',
        accDocNoFrom: '',
        accDocNoTo: '',
        fiscalYearFrom: '',
        fiscalYearTo: '',

        statusFrom: '',
        statusTo: '',
        unblockDate: '',

        fiAreaFrom: '',
        fiAreaTo: '',
        paymentCenterFrom: '',
        paymentCenterTo: '',
      };

      payload.compCodeFrom = form.departmentCodeFrom;
      payload.compCodeTo = form.departmentCodeTo;
      payload.accDocNoFrom = form.accDocNoFrom;
      payload.accDocNoTo = form.accDocNoTo;
      payload.fiscalYearFrom = this.utils.convertYearToAD(form.yearAccountFrom);
      payload.fiscalYearTo = this.utils.convertYearToAD(form.yearAccountTo);

      let unblockDate = '';
      if (form.unblockDate) {
        const day = form.unblockDate.getDate();
        const month = form.unblockDate.getMonth() + 1;
        const year = form.unblockDate.getFullYear();
        unblockDate = this.utils.parseDate(day, month, year);
      }

      payload.unblockDate = unblockDate;

      payload.statusFrom = form.statusFrom;
      payload.statusTo = form.statusTo;

      payload.fiAreaFrom = form.provinceCodeFrom;
      payload.fiAreaTo = form.provinceCodeTo;

      payload.paymentCenterFrom = form.disbursementCodeFrom;
      payload.paymentCenterTo = form.disbursementCodeTo;

      console.log(payload);

      this.search(payload);
    }
  }

  search(payload) {
    this.paymentBlockService.report(payload).then((result) => {
      this.report = result.data;

      console.log(result);
    });
  }

  openDialogSearchMaster(type): void {
    const dialogRef = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        this.omForm.patchValue({ [result.type]: result.value });
        if (type === 'departmentCodeFrom') {
        } else if (type === 'departmentCodeTo') {
        } else if (type === 'provinceCodeFrom') {
        } else if (type === 'provinceCodeTo') {
        } else if (type === 'vendorTaxIdFrom') {
        } else if (type === 'vendorTaxIdTo') {
        } else if (type === 'disbursementCodeFrom') {
        } else if (type === 'disbursementCodeTo') {
        } else if (type === 'payMethodFrom') {
        } else if (type === 'payMethodTo') {
        } else if (type === 'specialTypeFrom') {
        } else if (type === 'specialTypeTo') {
        }
      }
    });
  }

  // for clear input
  clearInput(inputForm) {
    this.omForm.controls[inputForm].setValue('');
  }

  viewDocument(item) {
    const url =
      './detail-fi-kb?compCode=' +
      item.companyCode +
      '&docNo=' +
      item.accDocNo +
      '&docYear=' +
      item.fiscalYear;
    window.open(url, 'name', 'width=1200,height=700');
  }

  openDialogCopyParameterComponent(type): void {
    const dialogRef = this.dialog.open(DialogCopyParameterComponent, {});

    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        const data = result.value;
        if (result.status === 'Save') {
          this.omForm.patchValue({
            paymentDate: new Date(data.paymentDate), // วันที่ประมวลผล
            paymentName: data.paymentName, // การกำหนด
          });
        }
      }
    });
  }
}
