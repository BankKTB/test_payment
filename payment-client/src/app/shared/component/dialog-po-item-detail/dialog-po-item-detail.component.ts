import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Utils } from '@shared/utils/utils';

export interface DialogData {
  itemText: any;
  checkStatus: boolean;
  egpData: any;
}

@Component({
  selector: 'app-dialog-po-item-detail',
  templateUrl: './dialog-po-item-detail.component.html',
  styleUrls: ['./dialog-po-item-detail.component.scss'],
})
export class DialogPoItemDetailComponent implements OnInit {
  dialogSpecifyGeneral: FormGroup;

  typeTextControl: FormControl; // ประเภทข้อความ
  additionalTextControl: FormControl; // ข้อความเพิ่มเติม

  documentNo = 1; // เลขลำดับ
  listDocument = [];
  isBtnDelete = true; // เช็คปุ่มdelete
  listValidate = [];
  selectListOrder = null; // เลือกหน้ารายการบัญชีที่เลือกจากตาราง
  isSelectOrder = false; // check is order select
  listHeaderText = [];
  isDisabledFromSearch = false;
  isCheckData = false;
  idSubmitedForm = false;
  isCheckEgp = false;

  constructor(
    public dialogRef: MatDialogRef<DialogPoItemDetailComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private formBuilder: FormBuilder,
    public utils: Utils
  ) {
    dialogRef.disableClose = true;
  }

  ngOnInit() {
    window.scrollTo(0, 0);
    // this.createFormControl();
    // this.createFormGroup();
    console.log(this.data.itemText);
    this.listHeaderText = [];

    if (this.data.itemText !== null && this.data.itemText.length > 0) {
      this.data.itemText.forEach((item) => {
        const data = {
          typeText: item.textCode,
          additionalText: item.textMsg,
        };
        this.listHeaderText.push(data);
      });
    }

    if (this.listHeaderText.length > 0) {
      this.setInputFormEdit(this.listHeaderText);
      this.isCheckData = true;
    }
    this.documentNo += this.listDocument.length;
    this.isDisabledFromSearch = this.data.checkStatus;
    // if (this.isDisabledFromSearch) {
    //   this.dialogSpecifyGeneral.disable();
    // } else {
    //   this.dialogSpecifyGeneral.enable();
    // }
  }

  createFormControl() {
    this.typeTextControl = this.formBuilder.control(this.constant.LIST_TYPE_GENERAL_TEXT[0].id); // ประเภทข้อความ
    this.additionalTextControl = this.formBuilder.control(''); // ข้อความเพิ่มเติม
  }

  createFormGroup() {
    this.dialogSpecifyGeneral = this.formBuilder.group({
      typeText: this.typeTextControl,
      additionalText: this.additionalTextControl,
    });
  }

  convertDateFormat(value) {
    if (value) {
      const allowedDate: RegExp = /^[\d]{4}-[\d]{2}-[\d]{2}/;
      if (allowedDate.test(value.toString())) {
        if (!isNaN(new Date(value).getTime())) {
          const date = new Date(value);
          return (
            date.getDate().toString().padStart(2, '0') +
            '-' +
            (date.getMonth() + 1).toString().padStart(2, '0') +
            '-' +
            date.getFullYear().toString()
          );
        } else {
          return value;
        }
      } else {
        return value;
      }
    }
  }

  saveEgpData(item) {
    if (item.podata.poContractSdate) {
      const data = {
        typeText: '',
        additionalText: '',
      };
      data.typeText = 'F01';
      data.additionalText = this.convertDateFormat(item.podata.poContractSdate);
      this.saveIntoList(data);
    }
    if (item.podata.proposalDate) {
      const data = {
        typeText: '',
        additionalText: '',
      };
      data.typeText = 'F03';
      data.additionalText = this.convertDateFormat(item.podata.proposalDate);
      this.saveIntoList(data);
    }
    if (item.podata.sendDate) {
      const data = {
        typeText: '',
        additionalText: '',
      };
      data.typeText = 'F04';
      data.additionalText = this.convertDateFormat(item.podata.sendDate);
      this.saveIntoList(data);
    }
    if (item.podata.dueDate) {
      const data = {
        typeText: '',
        additionalText: '',
      };
      data.typeText = 'F05';
      data.additionalText = this.convertDateFormat(item.podata.dueDate);
      this.saveIntoList(data);
    }
    if (item.podata.sendPlace) {
      const data = {
        typeText: '',
        additionalText: '',
      };
      data.typeText = 'F06';
      data.additionalText = this.convertDateFormat(item.podata.sendPlace);
      this.saveIntoList(data);
    }
    if (item.podata.warranteeYear && item.podata.warranteeMonth && item.podata.warranteeDay) {
      const data = {
        typeText: '',
        additionalText: '',
      };
      data.typeText = 'F07';
      data.additionalText =
        item.podata.warranteeYear +
        ' ' +
        item.podata.warranteeMonth +
        ' ' +
        item.podata.warranteeDay;
      this.saveIntoList(data);
    }
    if (item.podata.finePercent && item.podata.fineUnit) {
      const data = {
        typeText: '',
        additionalText: '',
      };
      data.typeText = 'F08';
      data.additionalText = item.podata.finePercent + '/' + item.podata.fineUnit;
      this.saveIntoList(data);
    }
    if (item.podata.fineunitPrice) {
      const data = {
        typeText: '',
        additionalText: '',
      };
      data.typeText = 'F09';
      data.additionalText = item.podata.fineunitPrice;
      this.saveIntoList(data);
    }
    if (item.podata.contractPoDate) {
      const data = {
        typeText: '',
        additionalText: '',
      };
      data.typeText = 'F11';
      data.additionalText = this.convertDateFormat(item.podata.contractPoDate);
      this.saveIntoList(data);
    }
    if (item.podata.corporateName) {
      const data = {
        typeText: '',
        additionalText: '',
      };
      data.typeText = 'F12';
      data.additionalText = item.podata.corporateName;
      this.saveIntoList(data);
    }
    if (item.podata.corporateDate) {
      const data = {
        typeText: '',
        additionalText: '',
      };
      data.typeText = 'F13';
      data.additionalText = this.convertDateFormat(item.podata.corporateDate);
      this.saveIntoList(data);
    }
    if (item.podata.authorityName) {
      const data = {
        typeText: '',
        additionalText: '',
      };
      data.typeText = 'F14';
      data.additionalText = item.podata.authorityName;
      this.saveIntoList(data);
    }
    if (item.podata.partnershipDate) {
      const data = {
        typeText: '',
        additionalText: '',
      };
      data.typeText = 'F15';
      data.additionalText = this.convertDateFormat(item.podata.partnershipDate);
      this.saveIntoList(data);
    }
    if (item.podata.numberDay) {
      const data = {
        typeText: '',
        additionalText: '',
      };
      data.typeText = 'F24';
      data.additionalText = item.podata.numberDay;
      this.saveIntoList(data);
    }
  }

  setInputFormEdit(items) {
    // const item = items[0];
    // this.dialogSpecifyGeneral.patchValue({
    //   typeText: item.typeText,
    //   additionalText: item.additionalText,
    // });
    this.listDocument = [];
    items.forEach((item) => {
      const typeTextName = this.constant.LIST_TYPE_GENERAL_TEXT.find(
        (items) => items.id === item.typeText
      ).name;
      const data = {
        typeText: item.typeText,
        typeTextName: typeTextName,
        additionalText: item.additionalText,
      };
      this.listDocument.push(data);
    });
  }

  addKeyValidate(value) {
    if (value.additionalText === '') {
      this.listValidate = ['กรุณาระบุข้อความเพิ่มเติม'];
    } else {
      this.listValidate = [];
    }
  }

  checkData() {
    if (this.listDocument.length > 0) {
      this.isCheckData = true;
    } else {
      this.isCheckData = false;
    }
  }

  checkEgpF01ToF13() {
    const value = this.dialogSpecifyGeneral.controls.typeText.value;
    if (this.data.egpData) {
      if (
        value === 'F01' ||
        value === 'F02' ||
        value === 'F03' ||
        value === 'F04' ||
        value === 'F05' ||
        value === 'F06' ||
        value === 'F07' ||
        value === 'F08' ||
        value === 'F09' ||
        value === 'F10' ||
        value === 'F11' ||
        value === 'F12' ||
        value === 'F13'
      ) {
        this.isCheckEgp = true;
      } else {
        this.isCheckEgp = false;
      }
    } else {
      this.isCheckEgp = false;
    }
  }

  keepOrder(value) {
    this.listValidate = [];
    this.addKeyValidate(value);
    // this.utils.checkValidateRequired(map, this.listValidate)
    if (this.listValidate.length <= 0) {
      if (this.documentNo > this.listDocument.length) {
        let indexItem = null;
        let count = 0;
        this.listDocument.forEach((element) => {
          if (element.typeText === value.typeText) {
            indexItem = count;
          }
          count++;
        });
        // const type = this.listDocument.find(item => item.typeText === value.typeText);
        if (indexItem !== null) {
          this.editItemFromType(indexItem, value);
        } else {
          this.saveIntoList(value);
        }
      } else {
        this.editIntoList(value);
      }
    }
    this.selectListOrder = null;
  }

  selectDocument(item, i, type) {
    this.selectListOrder = i + 1;
    this.documentNo = i + 1;
    this.isBtnDelete = false;
    this.setInputFormSelect(item);
    this.checkEgpF01ToF13();
  }

  editItemFromType(indexItem, value) {
    value.typeTextName = this.constant.LIST_TYPE_GENERAL_TEXT.find(
      (item) => item.id === value.typeText
    ).name;
    this.listDocument[indexItem] = value;
    this.documentNo = this.listDocument.length + 1;
    this.isBtnDelete = true;
    this.clearInput();
    this.checkData();
  }

  editIntoList(value) {
    value.typeTextName = this.constant.LIST_TYPE_GENERAL_TEXT.find(
      (item) => item.id === value.typeText
    ).name;
    this.listDocument[this.documentNo - 1] = value;
    this.documentNo = this.listDocument.length + 1;
    this.isBtnDelete = true;
    this.clearInput();
    this.checkData();
  }

  saveIntoList(value) {
    value.typeTextName = this.constant.LIST_TYPE_GENERAL_TEXT.find(
      (item) => item.id === value.typeText
    ).name;
    this.listDocument.push(value);
    this.documentNo = this.listDocument.length + 1;
    this.isBtnDelete = true;
    this.clearInput();
    this.checkData();
  }

  deleteOrder() {
    this.listDocument.splice(this.documentNo - 1, 1);
    this.documentNo = this.listDocument.length + 1;
    this.isBtnDelete = true;
    this.clearInput();
  }

  clearInput() {
    this.dialogSpecifyGeneral.patchValue({
      typeText: this.constant.LIST_TYPE_GENERAL_TEXT[0].id,
      additionalText: '',
    });
    this.checkEgpF01ToF13();
  }

  setInputFormSelect(item) {
    this.dialogSpecifyGeneral.patchValue({
      typeText: item.typeText,
      additionalText: item.additionalText,
    });
  }

  onNoClick(): void {
    this.dialogRef.close({ event: 'cancel' });
  }

  onClickSave() {
    console.log(this.listDocument);
    this.dialogRef.close({
      event: 'save',
      listHeaderText: this.listDocument,
    });
  }
}
