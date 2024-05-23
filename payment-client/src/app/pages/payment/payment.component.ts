import { Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { MatDialog, MatTabChangeEvent } from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { DialogSaveParameterComponent } from '@shared/component/dialog-status/dialog-save-parameter/dialog-save-parameter.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TabStatusComponent } from '@shared/component/tab-status/tab-status.component';
import { TabParameterComponent } from '@shared/component/tab-parameter/tab-parameter.component';
import { TabAdditionalLogComponent } from '@shared/component/tab-additional-log/tab-additional-log.component';
import { TabIndependentComponent } from '@shared/component/tab-independent/tab-independent.component';
import { Utils } from '@shared/utils/utils';
import { PaymentAliasService } from '@core/services/payment-alias/payment-alias.service';
import { DialogCopyParameterComponent } from '@shared/component/dialog-copy-parameter/dialog-copy-parameter.component';
import { MasterService, SidebarService } from '@core/services';
import { DialogSearchParameterComponent } from '@shared/component/dialog-search-parameter/dialog-search-parameter.component';

@Component({
  selector: 'app-home',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss'],
})
export class PaymentComponent implements OnInit {
  @ViewChildren('paymentInput') paymentInput: QueryList<ElementRef>;
  listObjectParameterTab: any = null;
  listObjectAdditionLogTab: any = null;
  listObjectIndependentTab: any = null;
  isDisabledTab: boolean = true;
  homeForm: FormGroup;

  // listObjectParameterTabForPayment: object = null
  paymentDateControl: FormControl; // วันที่ประมวลผล
  // listObjectAdditionLogTabForPayment: object = null
  paymentNameControl: FormControl; // การกำหนด
  // listObjectIndependentTabForPayment: object = null
  isDisabledCopy = false;
  paymentCondition = null;
  tabSelectedIndex = 0;

  isSubmitedForm = false;

  listValidate = [];

  // subscription;
  nonBusinessDays = [];
  @ViewChild(TabStatusComponent, { static: true })
  public tabStatusComponent: TabStatusComponent;
  @ViewChild(TabParameterComponent, { static: true })
  private tabParameterComponent: TabParameterComponent;
  @ViewChild(TabAdditionalLogComponent, { static: true })
  private tabAdditionalLogComponent: TabAdditionalLogComponent;
  @ViewChild(TabIndependentComponent, { static: true })
  private tabIndependentComponent: TabIndependentComponent;

  constructor(
    private dialog: MatDialog,
    private router: Router,
    private utils: Utils,
    private formBuilder: FormBuilder,
    private paymentAliasService: PaymentAliasService,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute,
    private sidebarService: SidebarService,
    private masterService: MasterService
  ) {
    this.createHomeFormControl();
    this.createHomeFormGroup();
    this.defaultHomeForm();
    this.route.queryParams.subscribe((params) => {
      if (params.paymentDate && params.paymentName) {
        let patchValueFromParams = async () => {
          this.homeForm.patchValue({
            paymentDate: new Date(JSON.parse(params.paymentDate)),
            paymentName: params.paymentName,
          });
        };
        patchValueFromParams().then(() => {
          // this.searchParameterAfterCreateOrUpdate();
          this.onBlurPaymentName();
        });
      }
    });
  }

  ngOnInit() {
    this.sidebarService.updatePageType('payment');
    this.sidebarService.updateNowPage('payment');
    localStorage.setItem('parameterTabForPayment', null);
    localStorage.setItem('independentTabForPayment', null);
    localStorage.setItem('additionLogTabForPayment', null);

    this.tabStatusComponent.showStatus(false, []);
    this.getNonBusinessDay();
    // const  text = '(33333,44444),30000,(33333,44444),12005,32005,(33333,44444)';
    // this.utils.convertCompanyTextToCompanyArray(text);
    // this.utils.chanTest();

    // this.subscription = interval(1000).subscribe(() => {
    //   const formValue = this.homeForm.value;
    //   if (formValue.paymentDate && formValue.paymentName) {
    //     this.isDisabledTab = false;
    //     this.searchPaymentDetailFromCopy(formValue, 'search');
    //   } else {
    //     this.isDisabledTab = true;
    //   }
    // });
  }

  // ngOnDestroy() {
  //   this.subscription.unsubscribe();
  // }

  getNonBusinessDay() {
    const nonBusinessDaysList = JSON.parse(sessionStorage.getItem('nonBusinessDays'));
    if (!nonBusinessDaysList) {
      this.masterService.findAllNonBusinessDay().then((result) => {
        if (result.status === 200) {
          const items = result.data;
          items.forEach((item) => {
            this.nonBusinessDays.push(item.date);
          });
          sessionStorage.setItem('nonBusinessDays', JSON.stringify(this.nonBusinessDays));
        }
      });
    }
  }

  createHomeFormControl() {
    this.paymentDateControl = this.formBuilder.control(''); // วันที่ประมวลผล
    this.paymentNameControl = this.formBuilder.control(''); // การกำหนด
  }

  createHomeFormGroup() {
    this.homeForm = this.formBuilder.group({
      paymentDate: this.paymentDateControl, // วันที่ประมวลผล
      paymentName: this.paymentNameControl, // การกำหนด
    });
  }

  defaultHomeForm() {
    this.homeForm.patchValue({
      paymentDate: '', // วันที่ประมวลผล
      paymentName: '', // การกำหนด
    });

    this.isDisabledTab = true;
  }

  clearInputAll() {
    localStorage.setItem('parameterTabForPayment', null);
    localStorage.setItem('independentTabForPayment', null);
    localStorage.setItem('additionLogTabForPayment', null);
    this.tabSelectedIndex = 0;
    this.defaultHomeForm();
    this.tabParameterComponent.defaultInput();
    this.tabIndependentComponent.defaultIndependet();
    this.tabAdditionalLogComponent.defaultInputAdditionLogForm();
    this.isDisabledCopy = false;
    this.tabStatusComponent.showStatus(false, []);
  }

  clearParameterInput() {
    this.tabParameterComponent.defaultInput();
    this.tabIndependentComponent.defaultIndependet();
    this.tabAdditionalLogComponent.defaultInputAdditionLogForm();
    this.tabStatusComponent.proposalStatus = null;
    this.isDisabledCopy = false;
    this.tabStatusComponent.showStatus(false, []);
  }

  receiveActionFromStatus() {
    console.log('aaaaaaaa');
    const formValue = this.homeForm.value;
    if (formValue.paymentDate && formValue.paymentName) {
      this.isDisabledTab = false;
      this.searchPaymentDetailFromCopy(formValue, 'search');
    } else {
      this.isDisabledTab = true;
    }
  }

  receiveObjectFromParameter($event) {
    const data = $event;
    if (data.copy) {
      this.searchPaymentDetailFromCopy(data, 'copy');
    } else {
      this.listObjectParameterTab = data;
    }
  }

  receiveObjectFromAdditionLog($event) {
    this.listObjectAdditionLogTab = $event;
  }

  receiveObjectFromIndependent($event) {
    this.listObjectIndependentTab = $event;
  }

  tabChanged(tabChangeEvent: MatTabChangeEvent): void {
    this.tabSelectedIndex = tabChangeEvent.index;
    if (this.tabSelectedIndex === 0) {
      this.tabParameterComponent.updateParameter();
      this.tabAdditionalLogComponent.updateParameter();
      this.tabIndependentComponent.updateParameter();
      const formValue = this.homeForm.value;

      const parameterTabForPaymentString = localStorage.getItem('parameterTabForPayment');

      const listObjectParameterTabString = JSON.stringify(this.listObjectParameterTab);
      console.log('asd');
      console.log(this.listObjectParameterTab);

      const independentTabForPaymentString = localStorage.getItem('independentTabForPayment');
      const listObjectIndependentTabString = JSON.stringify(this.listObjectIndependentTab);

      const additionLogTabForPaymentString = localStorage.getItem('additionLogTabForPayment');
      const listObjectAdditionLogTabString = JSON.stringify(this.listObjectAdditionLogTab);
      this.paymentAliasService
        .checkValidate(this.listObjectParameterTab)
        .then((result) => {
          if (result.status === 200) {
            this.listValidate = [];
          } else if (result.status === 422) {
            this.listValidate = result.error.data;

            this.tabSelectedIndex = 1;
            this.snackBar.open('กรุณาตรวจสอบ รหัสบริษัท หรือ วิธีชำระเงิน  ', '', {
              panelClass: '_warning',
            });
            return;
          }
        })
        .then(() => {
          if (this.listValidate.length <= 0) {
            if (!this.tabStatusComponent.proposalStatus && !this.tabStatusComponent.runStatus) {
              if (formValue.paymentName.length !== 5) {
                this.snackBar.open('กรุณาระบุบ ชื่อให้ครบ 5 ตัวอักษร   ', '', {
                  panelClass: '_warning',
                });
                return;
              }

              let checkVendorParameter = false;
              for (const item of this.listObjectParameterTab.vendor) {
                if (item.vendorTaxIdFrom) {
                  checkVendorParameter = false;
                  break;
                } else {
                  checkVendorParameter = true;
                  this.tabParameterComponent.checkParameterVendor();
                }
              }
              if (
                !this.listObjectParameterTab.company ||
                !this.listObjectParameterTab.postDate ||
                !this.listObjectParameterTab.paymentMethod ||
                !this.listObjectParameterTab.paymentDate ||
                checkVendorParameter
              ) {
                this.isSubmitedForm = true;
                this.tabSelectedIndex = 1;
                this.snackBar.open('กรุณาระบุ ข้อมูล   ', '', {
                  panelClass: '_warning',
                });
                return;
              } else {
                this.isSubmitedForm = false;
              }

              if (this.listObjectParameterTab.paymentDate < this.listObjectParameterTab.postDate) {
                this.isSubmitedForm = true;
                this.tabSelectedIndex = 1;
                this.snackBar.open('วันชำระถัดไปต้องมากกว่าวันที่ผ่านรายการปัจจุบัน', '', {
                  panelClass: '_warning',
                });
                return;
              } else {
                this.isSubmitedForm = false;
              }

              if (
                this.listObjectAdditionLogTab.checkBoxDueDate ||
                this.listObjectAdditionLogTab.checkBoxPaymentMethodAll ||
                this.listObjectAdditionLogTab.checkBoxPaymentMethodUnSuccess ||
                this.listObjectAdditionLogTab.checkBoxDisplayDetail
              ) {
                if (this.listObjectAdditionLogTab.vendor.length > 0) {
                  let checkVendorAddition = false;
                  // this.listObjectAdditionLogTab.vendor.forEach(item => {
                  for (const item of this.listObjectAdditionLogTab.vendor) {
                    if (item.vendorTaxIdFrom) {
                      checkVendorAddition = false;
                      break;
                    } else {
                      checkVendorAddition = true;
                      this.tabAdditionalLogComponent.checkAdditionVendor();
                    }
                  }

                  if (checkVendorAddition) {
                    this.isSubmitedForm = true;
                    this.tabSelectedIndex = 3;
                    this.snackBar.open('กรุณาระบุ ข้อมูลผู้ขาย ใน log เพิ่มเติม  ', '', {
                      panelClass: '_warning',
                    });
                  } else {
                    this.isSubmitedForm = false;
                    if (
                      (parameterTabForPaymentString !== listObjectParameterTabString ||
                        independentTabForPaymentString !== listObjectIndependentTabString ||
                        additionLogTabForPaymentString !== listObjectAdditionLogTabString) &&
                      formValue.paymentDate &&
                      formValue.paymentName
                    ) {
                      const dialogRef = this.dialog.open(DialogSaveParameterComponent, {});

                      dialogRef.afterClosed().subscribe((result) => {
                        if (result.event) {
                          if (result.value === 'Save') {
                            this.paymentCondition = {
                              parameter: this.listObjectParameterTab,
                              independent: this.listObjectIndependentTab,
                              additionLog: this.listObjectAdditionLogTab,
                            };
                            localStorage.setItem(
                              'parameterTabForPayment',
                              JSON.stringify(this.listObjectParameterTab)
                            );
                            localStorage.setItem(
                              'independentTabForPayment',
                              JSON.stringify(this.listObjectIndependentTab)
                            );
                            localStorage.setItem(
                              'additionLogTabForPayment',
                              JSON.stringify(this.listObjectAdditionLogTab)
                            );
                            this.searchParameterAfterCreateOrUpdate();
                          } else if (result.value === 'UnSave') {
                            this.tabParameterComponent.getParameterFromCopy(
                              JSON.parse(localStorage.getItem('parameterTabForPayment'))
                            );
                            this.tabIndependentComponent.getIndependentFromCopy(
                              JSON.parse(localStorage.getItem('independentTabForPayment'))
                            );
                            this.tabAdditionalLogComponent.getAdditionLogFromCopy(
                              JSON.parse(localStorage.getItem('additionLogTabForPayment'))
                            );
                          }
                        }
                      });
                    }
                  }
                }
              } else {
                if (
                  (parameterTabForPaymentString !== listObjectParameterTabString ||
                    independentTabForPaymentString !== listObjectIndependentTabString ||
                    additionLogTabForPaymentString !== listObjectAdditionLogTabString) &&
                  formValue.paymentDate &&
                  formValue.paymentName
                ) {
                  const dialogRef = this.dialog.open(DialogSaveParameterComponent, {});

                  dialogRef.afterClosed().subscribe((result) => {
                    if (result.event) {
                      if (result.value === 'Save') {
                        this.paymentCondition = {
                          parameter: this.listObjectParameterTab,
                          independent: this.listObjectIndependentTab,
                          additionLog: this.listObjectAdditionLogTab,
                        };
                        localStorage.setItem(
                          'parameterTabForPayment',
                          JSON.stringify(this.listObjectParameterTab)
                        );
                        localStorage.setItem(
                          'independentTabForPayment',
                          JSON.stringify(this.listObjectIndependentTab)
                        );
                        localStorage.setItem(
                          'additionLogTabForPayment',
                          JSON.stringify(this.listObjectAdditionLogTab)
                        );
                        this.searchParameterAfterCreateOrUpdate();
                      } else if (result.value === 'UnSave') {
                        // console.log(localStorage.getItem('parameterTabForPayment'));
                        this.tabParameterComponent.getParameterFromCopy(
                          JSON.parse(localStorage.getItem('parameterTabForPayment'))
                        );
                        this.tabIndependentComponent.getIndependentFromCopy(
                          JSON.parse(localStorage.getItem('independentTabForPayment'))
                        );
                        this.tabAdditionalLogComponent.getAdditionLogFromCopy(
                          JSON.parse(localStorage.getItem('additionLogTabForPayment'))
                        );
                      }
                    }
                  });
                }
              }
            }
          }
        });
    }
  }

  openDialogSearchParameterComponent(): void {
    const dialog = this.dialog.open(DialogSearchParameterComponent, {});

    dialog.afterClosed().subscribe((result) => {
      console.log('The dialog was closed');
      if (result && result.event) {
        this.homeForm.patchValue({
          paymentDate: new Date(result.paymentDate), // วันที่ประมวลผล
          paymentName: result.paymentName, // การกำหนด
        });
        this.onBlurPaymentName();
      }
    });
  }

  openDialogCopyParameterComponent(type): void {
    const dialogRef = this.dialog.open(DialogCopyParameterComponent, {});

    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        const data = result.value;
        if (result.status === 'Save') {
          if (type === 'copy') {
            this.searchPaymentDetailFromCopy(data, 'copy');
          } else if (type === 'search') {
            this.homeForm.patchValue({
              paymentDate: new Date(data.paymentDate), // วันที่ประมวลผล
              paymentName: data.paymentName, // การกำหนด
            });
            if (data.paymentDate && data.paymentName) {
              this.isDisabledTab = false;
            } else {
              this.isDisabledTab = true;
            }
            this.searchPaymentDetailFromCopy(data, 'search');
          }
        }
      }
    });
  }

  onPaymentDate() {
    const formValue = this.homeForm.value;
    if (formValue.paymentDate && formValue.paymentName && formValue.paymentName.length === 5) {
      this.isDisabledTab = false;
      this.searchPaymentDetailFromCopy(formValue, 'search');
    } else {
      this.isDisabledTab = true;
      this.tabSelectedIndex = 0;
    }
  }

  onBlurPaymentName(paymentName?: string) {
    if (paymentName) {
      this.homeForm.patchValue({
        paymentName,
      });
    }
    const formValue = this.homeForm.value;
    if (formValue.paymentDate && formValue.paymentName && formValue.paymentName.length === 5) {
      this.isDisabledTab = false;
      this.searchPaymentDetailFromCopy(formValue, 'search');
    } else {
      this.isDisabledTab = true;
      this.tabSelectedIndex = 0;
    }
  }

  searchPaymentDetailFromCopy(copy, type) {
    console.log('searchPaymentDetailFromCopy', copy, type);
    console.log(copy);
    const value = copy;
    const date = new Date(value.paymentDate);
    const dayPaymentDate = date.getDate();
    const monthPaymentDate = date.getMonth() + 1;
    const yearPaymentDate = date.getFullYear();
    const paymentDate = this.utils.parseDate(dayPaymentDate, monthPaymentDate, yearPaymentDate);
    const paymentName = value.paymentName;
    this.paymentAliasService.search(paymentDate, paymentName).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const formValue = this.homeForm.value;
        const data = result.data;
        if (data) {
          // console.log('The dialog was closed');
          const jsonObject = JSON.parse(data.jsonText);
          const proposalStatus = data.proposalStatus;
          const proposalJobStatus = data.proposalJobStatus;
          const runStatus = data.runStatus;
          const runJobStatus = data.runJobStatus;

          if (type === 'search') {
            if (proposalJobStatus) {
              this.isDisabledCopy = true;
            } else if (!proposalJobStatus && !proposalStatus) {
              this.isDisabledCopy = false;
            } else if (proposalStatus) {
              this.isDisabledCopy = true;
            } else if (!proposalStatus) {
              this.isDisabledCopy = false;
            } else if (runJobStatus) {
              this.isDisabledCopy = true;
            } else if (!runJobStatus && !runStatus) {
              this.isDisabledCopy = false;
            } else if (runStatus) {
              this.isDisabledCopy = true;
            } else if (!runStatus) {
              this.isDisabledCopy = false;
            }
          } else {
            this.isDisabledCopy = false;
          }
          this.listObjectParameterTab = jsonObject.parameter as any;
          this.listObjectIndependentTab = jsonObject.independent;
          this.listObjectAdditionLogTab = jsonObject.additionLog;

          console.log('data old {}', data);
          console.log('listObjectParameterTab {}', this.listObjectParameterTab);
          if (copy.adjustDate) {
            const paymentProcessDate = new Date(data.paymentDate);
            const parameterPostDate = new Date(this.listObjectParameterTab.postDate);
            parameterPostDate.setHours(0);
            const parameterSaveDate = new Date(this.listObjectParameterTab.saveDate);
            parameterSaveDate.setHours(0);
            const parameterPaymentDate = new Date(this.listObjectParameterTab.paymentDate);
            parameterPaymentDate.setHours(0);

            const amountPostDate = this.calculateDateDiff(parameterPostDate, paymentProcessDate);
            const amountSaveDate = this.calculateDateDiff(parameterSaveDate, paymentProcessDate);
            const amountPaymentDate = this.calculateDateDiff(
              parameterPaymentDate,
              paymentProcessDate
            );
            console.log('-------');
            console.log(amountPostDate);
            console.log(amountSaveDate);
            console.log(amountPaymentDate);
            console.log(formValue.paymentDate);
            if (formValue.paymentDate) {
              this.listObjectParameterTab.postDate =
                amountPostDate > 0
                  ? new Date(formValue.paymentDate).setDate(
                      formValue.paymentDate.getDate() + amountPostDate
                    )
                  : formValue.paymentDate;
              this.listObjectParameterTab.saveDate =
                amountSaveDate > 0
                  ? new Date(formValue.paymentDate).setDate(
                      formValue.paymentDate.getDate() + amountSaveDate
                    )
                  : formValue.paymentDate;
              this.listObjectParameterTab.paymentDate =
                amountPaymentDate > 0
                  ? new Date(formValue.paymentDate).setDate(
                      formValue.paymentDate.getDate() + amountPaymentDate
                    )
                  : formValue.paymentDate;

              // const payDate = new Date(formValue.paymentDate);
              // payDate.setDate(payDate.getDate() + 1);
              // console.log(payDate);
              // this.listObjectParameterTab.paymentDate = payDate;
            }
          }
          localStorage.setItem(
            'parameterTabForPayment',
            JSON.stringify(this.listObjectParameterTab)
          );
          localStorage.setItem(
            'independentTabForPayment',
            JSON.stringify(this.listObjectIndependentTab)
          );
          localStorage.setItem(
            'additionLogTabForPayment',
            JSON.stringify(this.listObjectAdditionLogTab)
          );
          this.tabParameterComponent.getParameterFromCopy(this.listObjectParameterTab);
          this.tabIndependentComponent.getIndependentFromCopy(this.listObjectIndependentTab);
          this.tabAdditionalLogComponent.getAdditionLogFromCopy(this.listObjectAdditionLogTab);
          if (type === 'search') {
            this.tabStatusComponent.showStatus(true, data);
          } else {
            console.log('data');
            console.log(data);
            data.parameterStatus = null;
            data.proposalStatus = null;
            data.runStatus = null;
            localStorage.setItem('parameterTabForPayment', null);
            localStorage.setItem('independentTabForPayment', null);
            localStorage.setItem('additionLogTabForPayment', null);
            this.tabStatusComponent.showStatus(false, data);
          }
        }
      } else if (result.status === 404) {
        const data = result.error;
        this.tabStatusComponent.showStatus(false, data);
        this.clearParameterInput();
      }
    });
  }

  searchParameterAfterCreateOrUpdate() {
    const formValue = this.homeForm.value;
    const date = new Date(formValue.paymentDate);
    const dayPaymentDate = date.getDate();
    const monthPaymentDate = date.getMonth() + 1;
    const yearPaymentDate = date.getFullYear();
    const paymentDate = this.utils.parseDate(dayPaymentDate, monthPaymentDate, yearPaymentDate);
    const paymentName = formValue.paymentName;
    this.paymentAliasService.search(paymentDate, paymentName).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.updateParameter(this.paymentCondition, data);
          this.tabStatusComponent.showStatus(true, data);
        }
      } else if (result.status === 404) {
        const data = result.error;
        this.createParameter(this.paymentCondition);
        this.tabStatusComponent.showStatus(false, data);
      }
    });
  }

  createParameter(jsonObject) {
    const formValue = this.homeForm.value;
    const data = {
      paymentDate: formValue.paymentDate,
      paymentName: formValue.paymentName,
      jsonText: JSON.stringify(jsonObject),
    };
    // console.log('createParameter');
    // console.log(data);
    this.paymentAliasService.create(data).then((result) => {
      this.searchPaymentDetail();
    });
  }

  updateParameter(jsonObject, response) {
    const formValue = this.homeForm.value;
    const data = {
      id: response.id,
      paymentDate: formValue.paymentDate,
      paymentName: formValue.paymentName,
      jsonText: JSON.stringify(jsonObject),
    };

    this.paymentAliasService.update(data, response.id).then((result) => {
      this.searchPaymentDetail();
    });
  }

  // deleteProposal() {
  //   const formValue = this.homeForm.value;
  //   const data = {
  //     paymentDate: formValue.paymentDate,
  //     paymentName: formValue.paymentName,
  //     jsonText: JSON.stringify(jsonObject),
  //   };
  //   console.log(data);
  //   this.paymentAliasService.update(data, response.id).then(result => {
  //     this.searchPaymentDetail();
  //     console.log(result);
  //   });
  // }
  searchPaymentDetail() {
    const formValue = this.homeForm.value;
    const date = new Date(formValue.paymentDate);
    const dayPaymentDate = date.getDate();
    const monthPaymentDate = date.getMonth() + 1;
    const yearPaymentDate = date.getFullYear();
    const paymentDate = this.utils.parseDate(dayPaymentDate, monthPaymentDate, yearPaymentDate);
    const paymentName = formValue.paymentName;
    this.paymentAliasService.search(paymentDate, paymentName).then((result) => {
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.tabStatusComponent.showStatus(true, data);
        }
      } else if (result.status === 404) {
        const data = result.error;
        this.tabStatusComponent.showStatus(false, data);
      }
    });
  }

  calculateDateDiff(paymentProcessDate, anotherDate) {
    const diffInMs = paymentProcessDate - anotherDate;
    return diffInMs / (1000 * 60 * 60 * 24);
  }

  // Input Payment name like insert
  replaceAt(textStr, index, character): string {
    return textStr.substr(0, index) + character + textStr.substr(index + character.length);
  }

  setSelectionRange(input, selectionStart, selectionEnd) {
    if (input.setSelectionRange) {
      input.focus();
      input.setSelectionRange(selectionStart, selectionEnd);
    } else if (input.createTextRange) {
      const range = input.createTextRange();
      range.collapse(true);
      range.moveEnd('character', selectionEnd);
      range.moveStart('character', selectionStart);
      range.select();
    }
  }

  onChangePaymentName(event) {
    const value = event.target.value;
    const countCurrent = value.length || 0;
    const position = event.target.selectionStart;
    if (countCurrent < 6 && value[event.target.selectionStart]) {
      if (/[A-Za-z0-9'-]/i.test(String.fromCharCode(event.which)) || event.which === 189) {
        const char = event.which === 189 ? '-' : String.fromCharCode(event.which);
        this.homeForm.controls.paymentName.setValue(
          this.replaceAt(value, event.target.selectionStart, char)
        );
        setTimeout(() => {
          this.paymentInput.toArray()[0].nativeElement.focus();
          this.setSelectionRange(
            this.paymentInput.toArray()[0].nativeElement,
            position + 1,
            position + 1
          );
        }, 0);
      }
    } else {
      this.paymentInput.toArray()[0].nativeElement.focus();
      this.setSelectionRange(
        this.paymentInput.toArray()[0].nativeElement,
        position + 1,
        position + 1
      );
    }
  }
}
