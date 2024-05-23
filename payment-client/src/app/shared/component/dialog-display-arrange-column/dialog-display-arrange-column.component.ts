import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { Utils } from '@shared/utils/utils';
import { ArrangeResponse } from '@core/models/arrange/arrange-response';
import { ReturnService } from '@core/services/return/return.service';
import { Sort } from '@core/models/sort';
import { ArrangeService } from '@core/services/arrange/arrange.service';
export interface DialogData {
  type: string;
  arrangeId: any;
}
@Component({
  selector: 'app-dialog-arrange-column',
  templateUrl: './dialog-display-arrange-column.component.html',
  styleUrls: ['./dialog-display-arrange-column.component.scss'],
})
export class DialogDisplayArrangeColumnComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  listArrange = [
    // {id: 1, arrangeCode: '001', arrangeName: 'ตัวอย่าง 1', arrangeDescription: 'รายละเอียด 1'},
    // {id: 2, arrangeCode: '002', arrangeName: 'ตัวอย่าง 2', arrangeDescription: 'รายละเอียด 2'},
    // {id: 3, arrangeCode: '003', arrangeName: 'ตัวอย่าง 3', arrangeDescription: 'รายละเอียด 3'},
  ];
  dataSource = new MatTableDataSource<ArrangeResponse>([]);
  columnsDisplay = [
    'id',
    'arrangeName',
    'arrangeDescription',
    'arrangeCode',
    // {
    //   key: 'id',
    //   text: 'No.',
    //   sequence: 1,
    //   hide: false,
    // },
    // {
    //   key: 'arrangeName',
    //   text: 'ชื่อโครงร่าง',
    //   sequence: 2,
    //   hide: false,
    // },
    // {
    //   key: 'arrangeDescription',
    //   text: 'รายละเอียดโครงร่าง',
    //   sequence: 3,
    //   hide: false,
    // },
    // {
    //   key: 'arrangeCode',
    //   text: '',
    //   sequence: 4,
    //   hide: false,
    // },
  ];
  multiSort: Sort[] = [{ id: 'id', direction: 'ASC', sequence: 1 }];
  hasData = false;

  constructor(
    private dialogRef: MatDialogRef<DialogDisplayArrangeColumnComponent>,
    private snackBar: MatSnackBar,
    private utils: Utils,
    private returnService: ReturnService,
    private arrangeService: ArrangeService,
    @Inject(MAT_DIALOG_DATA) public dialogData: DialogData
  ) {}

  ngOnInit() {
    this.dataSource = new MatTableDataSource<ArrangeResponse>([]);
    this.dataSource = new MatTableDataSource<ArrangeResponse>(this.listArrange);
    this.getByArrangeCode();
  }

  closeDialog() {
    this.dialogRef.close({
      event: true,
      status: 'Close',
      value: '',
    });
  }

  getByArrangeCode() {
    this.arrangeService.findByArrangeCode(this.dialogData.type).then((response) => {
      if (!this.utils.isEmpty(response.data)) {
        if (response.data.length > 0) {
          let objList: ArrangeResponse[] = [];
          response.data.forEach((value) => {
            objList.push({
              id: value.id,
              arrangeName: value.arrangeName,
              arrangeDescription: value.arrangeDescription,
              arrangeJsonText: value.arrangeJsonText,
              arrangeDefault: value.arrangeDefault,
            });
          });
          this.dataSource = new MatTableDataSource<ArrangeResponse>(objList);
          this.hasData = true;
        } else {
          this.dataSource = new MatTableDataSource<ArrangeResponse>();
          this.hasData = false;
        }
      }
    });
  }

  onSelect(element) {
    const objFound = this.dataSource.data.find((e) => e.id === element.id);
    let arrangeJsonText = null;
    if (!this.utils.isEmpty(objFound.arrangeJsonText)) {
      arrangeJsonText = JSON.parse(objFound.arrangeJsonText);
    }
    this.oneEditDefaultArrange(element.id);
    this.dialogRef.close({
      event: true,
      status: 'success',
      id: element.id,
      arrangeName: element.arrangeName,
      arrangeJsonText,
    });
  }

  oneEditDefaultArrange(id) {
    this.arrangeService.editDefaultArrange(id).then((response) => {
      console.log('oneEditDefaultArrange', id, response);
    });
  }

  onDelete(id) {
    this.arrangeService.delete(id).then((response) => {
      this.getByArrangeCode();
      this.snackBar.open('ลบข้อมูลสำเร็จ', 'close', {
        duration: 7000,
        panelClass: ['success-snackbar'],
      });
    });
  }
}
