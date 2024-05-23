import { Component, OnInit, ViewChild } from '@angular/core';
import {
  DateAdapter,
  MAT_DATE_FORMATS,
  MatDialog,
  MatSnackBar,
  MatSort,
  MatTableDataSource,
} from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Constant } from '@shared/utils/constant';
import { CompanyPayingPayMethodConfigService } from '@core/services/company-paying-pay-method-config/company-paying-pay-method-config.service';
import { CompanyPayingService } from '@core/services/company-paying/company-paying.service';

@Component({
  selector: 'app-company-paying-pay-method-config',
  templateUrl: './company-paying-pay-method-config.component.html',
  styleUrls: ['./company-paying-pay-method-config.component.scss'],
})
export class CompanyPayingPayMethodConfigComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  displayedColumns: string[] = ['companyCode', 'companyName', 'edit'];

  dataSource = new MatTableDataSource([]);

  companyPayingPayMethodConfigForm: FormGroup;

  companyPayingCodeControl: FormControl;
  payMethodControl: FormControl;

  maximumAmountControl: FormControl;
  minimumAmountControl: FormControl;

  allowedBankAnotherCountryControl: FormControl;
  allowedCurrencyAnotherCountryControl: FormControl;
  allowedPartnerAnotherCountryControl: FormControl;
  allowedSinglePaymentControl: FormControl;

  companyPayingPayMethodId: number;
  companyPayingId: number;

  companyPayingName = '';
  payMethodName;

  listCompany = [];

  constructor(
    public constant: Constant,
    private dialog: MatDialog,
    private router: Router,
    private formBuilder: FormBuilder,
    private companyPayingPayMethodConfigService: CompanyPayingPayMethodConfigService,
    private companyPayingService: CompanyPayingService,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      if (params) {
        console.log(params);
        this.companyPayingPayMethodId = params.companyPayingPayMethodId;
        this.companyPayingId = params.companyPayingId;
      }
    });
    this.createcompanyPayingPayMethodConfigFormControl();
    this.createcompanyPayingPayMethodConfigFormGroup();

    this.getCompanyPaying();
    this.searchByID();
  }

  getCompanyPaying() {
    this.companyPayingService.searchByID(this.companyPayingId).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.companyPayingPayMethodConfigForm.patchValue({
            companyPayingCode: data.companyCode,
          });
          this.companyPayingName = data.companyName;
        }
      } else if (result.status === 404) {
      }
    });
  }

  searchByID() {
    this.companyPayingPayMethodConfigService
      .searchByID(this.companyPayingPayMethodId)
      .then((result) => {
        console.log(result);
        if (result.status === 200) {
          const data = result.data;
          if (data) {
            this.companyPayingPayMethodConfigForm.patchValue({
              maximumAmount: data.maximumAmount,
              minimumAmount: data.minimumAmount,
              payMethod: data.payMethod,
              allowedBankAnotherCountry: data.allowedBankAnotherCountry,
              allowedCurrencyAnotherCountry: data.allowedCurrencyAnotherCountry,
              allowedPartnerAnotherCountry: data.allowedPartnerAnotherCountry,
              allowedSinglePayment: data.allowedSinglePayment,
            });
            this.companyPayingId = data.companyPayingId;
            this.payMethodName = data.payMethodName;
          }
        } else if (result.status === 404) {
        }
      });
  }

  createcompanyPayingPayMethodConfigFormControl() {
    this.companyPayingCodeControl = this.formBuilder.control('');

    this.maximumAmountControl = this.formBuilder.control('');
    this.minimumAmountControl = this.formBuilder.control('');
    this.payMethodControl = this.formBuilder.control('');

    this.allowedBankAnotherCountryControl = this.formBuilder.control('');
    this.allowedCurrencyAnotherCountryControl = this.formBuilder.control('');
    this.allowedPartnerAnotherCountryControl = this.formBuilder.control('');
    this.allowedSinglePaymentControl = this.formBuilder.control('');
  }

  createcompanyPayingPayMethodConfigFormGroup() {
    this.companyPayingPayMethodConfigForm = this.formBuilder.group({
      companyPayingCode: this.companyPayingCodeControl,

      maximumAmount: this.maximumAmountControl,
      minimumAmount: this.minimumAmountControl,
      payMethod: this.payMethodControl,

      allowedBankAnotherCountry: this.allowedBankAnotherCountryControl,
      allowedCurrencyAnotherCountry: this.allowedCurrencyAnotherCountryControl,
      allowedPartnerAnotherCountry: this.allowedPartnerAnotherCountryControl,
      allowedSinglePayment: this.allowedSinglePaymentControl,
    });
  }

  insertCompanyPayingPayMethodConfig(value) {
    console.log(value);

    if (!value.maximumAmount) {
      this.snackBar.open('กรุณากรอก จำนวนเงินสูงสุด', '', {
        panelClass: '_warning',
      });
      return;
    } else if (!value.minimumAmount) {
      this.snackBar.open('กรุณากรอก จำนวนเงินต่ำสุด', '', {
        panelClass: '_warning',
      });
      return;
    }
    const payload = {
      allowedBankAnotherCountry: value.allowedBankAnotherCountry,
      allowedCurrencyAnotherCountry: value.allowedCurrencyAnotherCountry,
      allowedPartnerAnotherCountry: value.allowedPartnerAnotherCountry,
      allowedSinglePayment: value.allowedSinglePayment,

      maximumAmount: value.maximumAmount,
      minimumAmount: value.minimumAmount,

      id: this.companyPayingPayMethodId,
    };
    this.companyPayingPayMethodConfigService
      .update(payload, this.companyPayingPayMethodId)
      .then((result) => {
        console.log(result);
        if (result.status === 201) {
          const data = result.data;
          if (data) {
            this.searchByID();

            this.snackBar.open('บันทึกข้อมูลสำเร็จ', '', {
              panelClass: '_success',
            });
          }
        } else if (result.status === 403) {
          this.snackBar.open('หน่วยงานนี้มีข้อมูลแล้ว', '', {
            panelClass: '_error',
          });
        }
      });
  }

  deleteInputCompanyCode(element) {
    const id = element.id;
    this.companyPayingPayMethodConfigService.delete(id).then((result) => {
      console.log(result);
      if (result.status === 201) {
        const data = result.data;
        if (data) {
          this.searchByID();
        }
      } else if (result.status === 404) {
      }
    });
  }

  routeToCompanyPayingBankConfig(element) {
    const id = element.id;
    this.router.navigate(['/companyPayingBankConfigComponent'], {
      queryParams: { companyPayingId: id },
    });
  }
}
