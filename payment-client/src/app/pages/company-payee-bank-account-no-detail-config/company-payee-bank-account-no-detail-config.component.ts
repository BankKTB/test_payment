import { Component, OnInit } from '@angular/core';
import { DateAdapter, MAT_DATE_FORMATS, MatDialog, MatSnackBar } from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { CompanyPayeeBankAccountNoConfigService } from '@core/services/company-payee-bank-account-no-config/company-payee-bank-account-no-config.service';
import { MasterService } from '@core/services';
import { CompanyPayeeService } from '@core/services/company-payee/company-payee.service';

@Component({
  selector: 'app-company-payee-bank-account-no-detail-config',
  templateUrl: './company-payee-bank-account-no-detail-config.component.html',
  styleUrls: ['./company-payee-bank-account-no-detail-config.component.scss'],
})
export class CompanyPayeeBankAccountNoDetailConfigComponent implements OnInit {
  companyPayeeBankAccountNoDetailConfigForm: FormGroup;

  companyPayeeCodeControl: FormControl;
  houseBankKeyControl: FormControl;
  accountCodeControl: FormControl;
  accountDescriptionControl: FormControl;
  address1Control: FormControl;
  address2Control: FormControl;
  address3Control: FormControl;
  address4Control: FormControl;
  address5Control: FormControl;
  bankAccountNoControl: FormControl;
  bankAccountNoEtcControl: FormControl;
  bankNameControl: FormControl;
  cityControl: FormControl;
  currencyControl: FormControl;
  glAccountControl: FormControl;
  keyControlControl: FormControl;
  swiftCodeControl: FormControl;
  zipCodeControl: FormControl;
  bankBranchControl: FormControl;
  countryControl: FormControl;
  id: number;
  listCompanyPayeeHouseBankKeyDetailConfig = [{}] as any;

  accountCode: string;
  houseBankKey: string;
  companyPayeeId: number;
  houseBankKeyId: number;

  companyPayeeName = '';
  bankBranchName = '';
  countryNameEn = '';

  constructor(
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    private companyPayeeBankAccountNoConfigService: CompanyPayeeBankAccountNoConfigService,
    private companyPayeeService: CompanyPayeeService,
    private masterService: MasterService,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      console.log('params');
      console.log(params);
      if (params) {
        this.id = params.bankAccountId;
        this.accountCode = params.accountCode;
        this.houseBankKey = params.houseBankKey;
        this.companyPayeeId = params.companyPayeeId;
      }
    });
    this.createCompanyPayeeBankAccountNoDetailConfigFormControl();
    this.createCompanyPayeeBankAccountNoDetailConfigFormGroup();
    this.getCompanyPayee();
  }

  createCompanyPayeeBankAccountNoDetailConfigFormControl() {
    this.companyPayeeCodeControl = this.formBuilder.control('');
    this.houseBankKeyControl = this.formBuilder.control('');

    this.accountCodeControl = this.formBuilder.control('');
    this.accountDescriptionControl = this.formBuilder.control('');
    this.address1Control = this.formBuilder.control('');
    this.address2Control = this.formBuilder.control('');
    this.address3Control = this.formBuilder.control('');
    this.address4Control = this.formBuilder.control('');
    this.address5Control = this.formBuilder.control('');
    this.bankAccountNoControl = this.formBuilder.control('');
    this.bankAccountNoEtcControl = this.formBuilder.control('');
    this.bankNameControl = this.formBuilder.control('');
    this.cityControl = this.formBuilder.control('');
    this.currencyControl = this.formBuilder.control('');
    this.glAccountControl = this.formBuilder.control('');
    this.keyControlControl = this.formBuilder.control('');
    this.swiftCodeControl = this.formBuilder.control('');
    this.zipCodeControl = this.formBuilder.control('');
    this.bankBranchControl = this.formBuilder.control('');
    this.countryControl = this.formBuilder.control('');
  }

  createCompanyPayeeBankAccountNoDetailConfigFormGroup() {
    this.companyPayeeBankAccountNoDetailConfigForm = this.formBuilder.group({
      companyPayeeCode: this.companyPayeeCodeControl,
      houseBankKey: this.houseBankKeyControl,

      accountCode: this.accountCodeControl,
      accountDescription: this.accountDescriptionControl,
      address1: this.address1Control,
      address2: this.address2Control,
      address3: this.address3Control,
      address4: this.address4Control,
      address5: this.address5Control,
      bankAccountNo: this.bankAccountNoControl,
      bankAccountNoEtc: this.bankAccountNoEtcControl,
      bankName: this.bankNameControl,
      city: this.cityControl,
      currency: this.currencyControl,
      glAccount: this.glAccountControl,
      keyControl: this.keyControlControl,
      swiftCode: this.swiftCodeControl,
      zipCode: this.zipCodeControl,
      bankBranch: this.bankBranchControl,
      country: this.countryControl,
    });
  }

  getCompanyPayee() {
    this.companyPayeeService.searchByID(this.companyPayeeId).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.companyPayeeBankAccountNoDetailConfigForm.patchValue({
            companyPayeeCode: data.companyCode,
          });
          this.companyPayeeName = data.companyName;
          this.getOneHouseBankByCompanyCode(data.companyCode);
        }
      } else if (result.status === 404) {
      }
    });
  }

  getOneHouseBankByCompanyCode(companyCode) {
    this.masterService
      .findOneHouseBankAccountWithParam(companyCode, this.houseBankKey, this.accountCode)
      .then((result) => {
        console.log(result);
        if (result.status === 200) {
          const data = result.data;
          if (data) {
            this.companyPayeeBankAccountNoDetailConfigForm.patchValue({
              accountCode: data.accountCode,
              accountDescription: data.accountDescription,

              bankAccountNo: data.bankAccountNo,
              currency: data.currency,
              glAccount: data.glAccount,

              address1: data.address1,
              address2: data.address2,
              address3: data.address3,
              address4: data.address4,
              address5: data.address5,
              bankBranch: data.bankBranch,
              bankName: data.bankName,
              city: data.city,
              country: data.countryCode,
              houseBankKey: data.valueCode,
              id: data.id,
              swiftCode: data.swiftCode,
              zipCode: data.zipCode,
            });
            this.countryNameEn = data.countryNameEn;
            this.bankBranchName = data.bankName;
          }
        } else if (result.status === 404) {
        }
      });
  }

  // searchByID() {
  //   this.companyPayeeBankAccountNoConfigService.searchByID(this.id).then(result => {
  //     console.log(result);
  //     if (result.status === 200) {
  //       const data = result.data;
  //       if (data) {
  //         this.companyPayeeBankAccountNoDetailConfigForm.patchValue({
  //           companyPayeeCode: data.companyPayee.companyCode,
  //           houseBankKey: data.houseBankKey,
  //           accountCode: data.accountCode,
  //           accountDescription: data.accountDescription,
  //           address1: data.address1,
  //           address2: data.address2,
  //           address3: data.address3,
  //           address4: data.address4,
  //           address5: data.address5,
  //           bankAccountNo: data.bankAccountNo,
  //           bankAccountNoEtc: data.bankAccountNoEtc,
  //           bankName: data.bankName,
  //           city: data.city,
  //           currency: data.currency,
  //           glAccount: data.glAccount,
  //           keyControl: data.keyControl,
  //           swiftCode: data.swiftCode,
  //           zipCode: data.zipCode,
  //           bankBranch: data.bankBranch,
  //           country: data.country,
  //         });
  //         this.accountCode = data.accountCode;
  //         this.houseBankKey = data.houseBankKey;
  //         this.companyPayeeId = data.companyPayee.id;
  //         this.companyPayeeName = data.companyPayee.companyName;
  //         this.bankBranchNAme = data.bankName;
  //         this.countryNameEn = data.countryNameEn;
  //       }
  //     } else if (result.status === 404) {
  //     }
  //   });
  // }

  // clickUpdate(value) {
  //   const payload = {
  //     accountCode: value.accountCode,
  //     accountDescription: value.accountDescription,
  //     address1: value.address1,
  //     address2: value.address2,
  //     address3: value.address3,
  //     address4: value.address4,
  //     address5: value.address5,
  //     bankAccountNo: value.bankAccountNo,
  //     bankAccountNoEtc: value.bankAccountNoEtc,
  //     bankName: value.bankName,
  //     city: value.city,
  //     currency: value.currency,
  //     glAccount: value.glAccount,
  //     keyControl: value.keyControl,
  //     swiftCode: value.swiftCode,
  //     zipCode: value.zipCode,
  //     bankBranch: value.bankBranch,
  //     country: value.country,
  //   };
  //   this.companyPayeeBankAccountNoConfigService.update(payload, this.id).then(result => {
  //     if (result.status === 201) {
  //       const data = result.data;
  //       if (data) {
  //         this.searchByID();
  //         this.openSnackBarSaveSuccess();
  //       }
  //     } else if (result.status === 404) {
  //     }
  //   });
  // }

  // openSnackBarSaveSuccess() {
  //   this.snackBar.open('บันทึกข้อมูลสำเร็จ', '', {
  //     panelClass: '_success',
  //   });
  // }

  // deleteItemCompanyPayeeHouseBankKeyDetailConfig(index) {
  //   const id = this.listCompanyPayeeHouseBankKeyDetailConfig[index].id;
  //   this.companyPayeeBankAccountNoConfigService.delete(id).then(result => {
  //     console.log(result);
  //     if (result.status === 201) {
  //       const data = result.data;
  //       if (data) {
  //         this.searchByID();
  //       }
  //     } else if (result.status === 404) {
  //     }
  //   });
  // }
}
