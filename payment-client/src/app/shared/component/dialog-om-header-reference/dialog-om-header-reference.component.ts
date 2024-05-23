import { Component, ElementRef, Inject, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
export interface DialogData {
  listHeaderReference: [{ from: null; to: null; optionExclude: false }];
}
@Component({
  selector: 'app-dialog-om-header-reference',
  templateUrl: './dialog-om-header-reference.component.html',
  styleUrls: ['./dialog-om-header-reference.component.scss']
})
export class DialogOmHeaderReferenceComponent implements OnInit {
  @ViewChildren('from') from: QueryList<ElementRef>;
  @ViewChildren('to') to: QueryList<ElementRef>;
  @ViewChildren('optionExclude') optionExclude: QueryList<ElementRef>;

  listHeaderReference = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listMessageResponse = [];

  constructor(
    public dialogRef: MatDialogRef<DialogOmHeaderReferenceComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private router: Router,
    private dialog: MatDialog
  ) {
    dialogRef.backdropClick().subscribe(() => {
      console.log('close dialog');
    });
  }

  ngOnInit() {
    console.log(this.data.listHeaderReference);
    if (this.data.listHeaderReference[0].from) {
      this.listHeaderReference = this.data.listHeaderReference;
    }
  }

  addInputCompany() {
    this.listHeaderReference.push({
      from: null,
      to: null,
      optionExclude: false,
    });
  }

  setHeaderReference(index) {
    const headerReferenceFrom = this.from.toArray()[index].nativeElement.value;
    const headerReferenceTo = this.to.toArray()[index].nativeElement.value;
    if (headerReferenceFrom) {
      this.listHeaderReference[index].from = headerReferenceFrom;
    } else {
      this.listHeaderReference[index].from = '';
    }
    if (headerReferenceTo) {
      this.listHeaderReference[index].to = headerReferenceTo;
    } else {
      this.listHeaderReference[index].to = '';
    }
  }

  setOptionInclude(index) {
    const valueIndex = this.optionExclude.toArray()[index] as any;

    const optionExclude = valueIndex._checked;
    if (optionExclude) {
      this.listHeaderReference[index].optionExclude = optionExclude;
    } else {
      this.listHeaderReference[index].optionExclude = false;
    }
  }

  deleteInputDocumentNo(index) {
    if (this.listHeaderReference.length > 1) {
      this.listHeaderReference.splice(index, 1);
    }
  }

  onNoClick() {
    this.dialogRef.close({
      event: true,
      status: 'Close',
      value: '',
    });
  }

  copyFromExcel(event, index, type) {
    // console.log(event);

    const value = event.data;

    // console.log('value' + value);

    if (value.length > 10) {
      // console.log('index' + index);

      console.log(value.split('\n'));
      const list = value.split('\n');

      if (list.length > this.listHeaderReference.length) {
        const need = list.length - this.listHeaderReference.length;
        for (let i = 0; i < need; i++) {
          this.listHeaderReference.push({
            from: null,
            to: null,
            optionExclude: false,
          });
        }
      }
      console.log(this.listHeaderReference);
      if (type === 'headerReferenceFrom') {
        for (let i = index; i < list.length; i++) {
          this.listHeaderReference[i].from = list[i];
        }
      } else if (type === 'headerReferenceTo') {
        for (let i = index; i < list.length; i++) {
          this.listHeaderReference[i].to = list[i];
        }
      }
    }
  }

  openDialogSearchDocumentNo(index, type): void {
    const dialog = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });
    dialog.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (type === 'headerReferenceFrom') {
          this.listHeaderReference[index].from = result.value;
        } else if (type === 'headerReferenceTo') {
          this.listHeaderReference[index].to = result.value;
        }
      }
    });
  }

  clickSave() {
    this.dialogRef.close({
      event: true,
      status: 'save',
      value: this.listHeaderReference,
    });
  }
}
