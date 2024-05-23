import { Component, OnInit } from '@angular/core';
import { DateAdapter, MAT_DATE_FORMATS, MatDialog, MatSnackBar } from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { CompanyPayeeHouseBankKeyConfigService } from '@core/services/company-payee-house-bank-key-config/company-payee-house-bank-key-config.service';
import { MasterService } from '@core/services';
import { CompanyPayeeService } from '@core/services/company-payee/company-payee.service';

@Component({
  selector: 'app-company-payee-house-bank-key-detail-config',
  templateUrl: './company-payee-house-bank-key-detail-config.component.html',
  styleUrls: ['./company-payee-house-bank-key-detail-config.component.scss'],
})
export class CompanyPayeeHouseBankKeyDetailConfigComponent implements OnInit {
  companyPayeeHouseBankKeyDetailConfigForm: FormGroup;

  companyPayeeCodeControl: FormControl;
  address1Control: FormControl;
  address2Control: FormControl;
  address3Control: FormControl;
  address4Control: FormControl;
  address5Control: FormControl;
  bankBranchControl: FormControl;
  bankNameControl: FormControl;
  cityControl: FormControl;

  countryControl: FormControl;
  houseBankKeyControl: FormControl;
  idControl: FormControl;
  swiftCodeControl: FormControl;
  zipCodControle: FormControl;
  id: number;
  companyPayeeId: string;
  houseBankKey: string;
  bankBranch: string;
  listCompanyPayeeHouseBankKeyDetailConfig = [{}] as any;

  companyPayeeName = '';
  bankBranchName = '';
  countryNameEn = '';

  constructor(
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    private companyPayeeHouseBankKeyConfigService: CompanyPayeeHouseBankKeyConfigService,
    private companyPayeeService: CompanyPayeeService,
    private masterService: MasterService,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      console.log('house config detail');
      console.log(params);
      if (params) {
        this.companyPayeeId = params.companyPayeeId;
        this.bankBranch = params.bankBranch;
        this.houseBankKey = params.houseBankKey;
      }
    });
    this.createcompanyPayeeHouseBankKeyDetailConfigFormControl();
    this.createcompanyPayeeHouseBankKeyDetailConfigFormGroup();
    this.getCompanyPayee();
  }

  createcompanyPayeeHouseBankKeyDetailConfigFormControl() {
    this.address1Control = this.formBuilder.control('');
    this.address2Control = this.formBuilder.control('');
    this.address3Control = this.formBuilder.control('');
    this.address4Control = this.formBuilder.control('');
    this.address5Control = this.formBuilder.control('');
    this.bankBranchControl = this.formBuilder.control('');
    this.bankNameControl = this.formBuilder.control('');
    this.cityControl = this.formBuilder.control('');
    this.companyPayeeCodeControl = this.formBuilder.control('');
    this.countryControl = this.formBuilder.control('');
    this.houseBankKeyControl = this.formBuilder.control('');
    this.idControl = this.formBuilder.control('');
    this.swiftCodeControl = this.formBuilder.control('');
    this.zipCodControle = this.formBuilder.control('');
  }

  createcompanyPayeeHouseBankKeyDetailConfigFormGroup() {
    this.companyPayeeHouseBankKeyDetailConfigForm = this.formBuilder.group({
      address1: this.address1Control,
      address2: this.address2Control,
      address3: this.address3Control,
      address4: this.address4Control,
      address5: this.address5Control,
      bankBranch: this.bankBranchControl,
      bankName: this.bankNameControl,
      city: this.cityControl,
      companyPayeeCode: this.companyPayeeCodeControl,
      country: this.countryControl,
      houseBankKey: this.houseBankKeyControl,
      id: this.idControl,
      swiftCode: this.swiftCodeControl,
      zipCode: this.zipCodControle,
    });
  }

  getCompanyPayee() {
    this.companyPayeeService.searchByID(this.companyPayeeId).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.companyPayeeHouseBankKeyDetailConfigForm.patchValue({
            companyPayeeCode: data.companyCode,
          });
          this.companyPayeeName = data.companyName;
          this.getAllHouseBankByCompanyCode(data.companyCode);
        }
      } else if (result.status === 404) {
      }
    });
  }

  getAllHouseBankByCompanyCode(companyCode) {
    this.masterService
      .findOneHouseBankWithParam(companyCode, this.houseBankKey, this.bankBranch)
      .then((result) => {
        console.log(result);
        if (result.status === 200) {
          const data = result.data;
          if (data) {
            this.companyPayeeHouseBankKeyDetailConfigForm.patchValue({
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
          // this.listPayeeHouseBankKeyConfig = [];
          // this.dataSource = new MatTableDataSource([]);
        }
      });
  }

  searchByID() {
    this.companyPayeeHouseBankKeyConfigService.searchByID(this.id).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.companyPayeeHouseBankKeyDetailConfigForm.patchValue({
            address1: data.address1,
            address2: data.address2,
            address3: data.address3,
            address4: data.address4,
            address5: data.address5,
            bankBranch: data.bankBranch,
            bankName: data.bankName,
            city: data.city,

            country: data.country,
            houseBankKey: data.houseBankKey,
            id: data.id,
            swiftCode: data.swiftCode,
            zipCode: data.zipCode,
          });
          // this.companyPayeeId = data.companyPayee.id;
          // this.houseBankKey = data.houseBankKey;
          // this.bankBranchNAme = data.bankName;
          // this.countryNameEn = data.countryNameEn;
        }
      } else if (result.status === 404) {
      }
    });
  }
}
