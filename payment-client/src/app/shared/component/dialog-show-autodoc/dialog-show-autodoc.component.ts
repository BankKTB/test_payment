import { Component, Inject, OnInit } from '@angular/core';
import { WebInfo } from '@core/models/web-info';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FiService } from '@core/services/fi/fi.service';
import { Constant } from '@shared/utils/constant';
import { LocalStorageService } from '@core/services';

export interface DialogData {
  page: any;
  autodocs: [];
  type: any;
  formID: any;
  header: any;
}

@Component({
  selector: 'app-dialog-show-autodoc',
  templateUrl: './dialog-show-autodoc.component.html',
  styleUrls: ['./dialog-show-autodoc.component.scss'],
})
export class DialogShowAutodocComponent implements OnInit {
  displayedColumns: string[] = [
    'list',
    'compCode',
    'docType',
    'accDocNo',
    'revDocType',
    'revAccDocNo',
    'fiscalYear',
  ];

  listResultPresave = [];
  resultSave = null;

  isPresaveSuccess = true;
  isSaveSuccess = false;
  allPage: any;
  pathPage: string;
  createPage: string;
  searchPage: string;
  backListPage: string;
  editPage: string;
  webInfo: WebInfo;
  xml: string;
  jsonString: string;

  isLoading = false;
  isDisableRefresh = true;
  isDisableClose = true;

  dataSource = [];
  type: string;
  formID: string;

  constructor(
    public dialogRef: MatDialogRef<DialogShowAutodocComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private fiService: FiService,
    private localStorageService: LocalStorageService,
  ) {
  }

  ngOnInit() {
    window.scrollTo(0, 0);
    this.allPage = this.data.page;
    this.dataSource = this.data.autodocs;
    this.type = this.data.type;
    this.formID = this.data.formID;
    console.log(this.data);

  }


  onClickClose(): void {
    this.dialogRef.close({ event: 'cancel' });
  }

  viewDocument(item) {
    if (this.type === 'kb') {
      const url =
        './detail-fi-kb?compCode=' +
        item.compCode +
        '&docNo=' +
        item.accDocNo +
        '&docYear=' +
        item.fiscalYear;
      window.open(url, 'name', 'width=1200,height=700');
    }
  }

  refresh() {
    this.dataSource = [];
    this.isDisableRefresh = true;
    this.isDisableClose = true;
    const yearTh = Number(this.data.header.fiscalYear);
    const payload = {
      compCode: this.data.header.compCode,
      accDocNo: this.data.header.accDocNo,
      fiscalYear: yearTh.toString(),
      formID: this.data.header.formID,
      webInfo: this.localStorageService.getWebInfo(),
    };

    this.fiService.searchAutoDoc(payload).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const autodocs = result.data.autoDoc as any;

        if (autodocs) {
          autodocs.forEach((element) => {
            this.dataSource.push(element);
            this.isDisableRefresh = false;
            this.isDisableClose = false;
          });
        }
      }
      {
        // TODO
      }
      // this.dataSource.push(result.data);
    });
  }

  onNoClick(): void {
    this.dialogRef.close({});
  }

  getDisplayedColumns() {
    // this.columnTable.sort((a, b) => (a.seq > b.seq ? 1 : -1));
    // return this.columnTable.filter((cd) => cd.showColumn).map((cd) => cd.key);
  }
}
