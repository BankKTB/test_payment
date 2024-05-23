import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ArrangeReques } from '@core/models/arrange/arrange-reques';
import { Utils } from '@shared/utils/utils';
import { ArrangeService } from '@core/services/arrange/arrange.service';
import { ArrangeResponse } from '@core/models/arrange/arrange-response';
import { MatTableDataSource } from '@angular/material/table';

export interface DialogData {
  listColumnShow: any;
  type: '';
  arrangeId: null;
}

@Component({
  selector: 'app-dialog-arrange-column',
  templateUrl: './dialog-update-arrange-column.component.html',
  styleUrls: ['./dialog-update-arrange-column.component.scss'],
})
export class DialogUpdateArrangeColumnComponent implements OnInit {
  arrangeForm: FormGroup;
  isSubmitedForm = false;
  listValidate = [];

  constructor(
    private dialogRef: MatDialogRef<DialogUpdateArrangeColumnComponent>,
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder,
    private utils: Utils,
    private arrangeService: ArrangeService,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    this.createFormGroup();
  }

  ngOnInit() {
    this.getByArrangeId();
  }

  createFormGroup() {
    this.arrangeForm = this.formBuilder.group({
      arrangeName: this.formBuilder.control('', [Validators.required]),
      arrangeDescription: this.formBuilder.control(''),
    });
  }

  getByArrangeId() {
    this.arrangeService.findByArrangeId(this.data.arrangeId).then((response) => {
      if (!this.utils.isEmpty(response.data)) {
        this.setDataInput('arrangeName', response.data.arrangeName);
        this.setDataInput('arrangeDescription', response.data.arrangeDescription);
      }
    });
  }

  setDataInput(inputForm, value) {
    this.arrangeForm.controls[inputForm].setValue(value);
  }

  clickSave() {
    const form = this.arrangeForm.value;
    this.listValidate = [];
    if (!form.arrangeName) {
      this.listValidate.push('กรุณาระบุชื่อโครงร่าง');
    }
    this.isSubmitedForm = true;
    if (this.listValidate.length <= 0) {
      this.isSubmitedForm = false;
      const body: ArrangeReques = {
        id: this.data.arrangeId ? this.data.arrangeId : null,
        arrangeCode: this.data.type,
        arrangeName: form.arrangeName,
        arrangeDescription: form.arrangeDescription,
        arrangeJsonText: JSON.stringify(this.data.listColumnShow),
      };
      if (!this.utils.isEmpty(this.data.arrangeId)) {
        this.update(body);
      }
    }
  }

  update(body: ArrangeReques) {
    this.arrangeService.update(body, this.data.arrangeId).then((response) => {
      if (response.status === 201) {
        this.snackBar.open('บันทึกข้อมูลสำเร็จ', 'close', {
          duration: 7000,
          panelClass: ['success-snackbar'],
        });
        let arrangeJsonText = null;
        if (!this.utils.isEmpty(response.data.arrangeJsonText)) {
          arrangeJsonText = JSON.parse(response.data.arrangeJsonText);
        }
        let obj: ArrangeResponse = {
          id: response.data.id,
          arrangeCode: response.data.arrangeCode,
          arrangeName: response.data.arrangeName,
          arrangeDescription: response.data.arrangeDescription,
          arrangeJsonText: arrangeJsonText,
        };
        this.dialogRef.close({
          event: true,
          status: 'success',
          data: obj,
        });
      } else {
      }
    });
  }

  closeDialog() {
    this.dialogRef.close({
      event: true,
      status: 'Close',
      value: '',
    });
  }
}
