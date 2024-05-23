import { Component, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild } from '@angular/core';
import { Constant } from '@shared/utils/constant';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Utils } from '@shared/utils/utils';
import { LoaderService, LocalStorageService } from '@core/services';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { MatPaginator } from '@angular/material/paginator';
import { ReturnService } from '@core/services/return/return.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DialogFilterColumnComponent } from '@shared/component/dialog-filter-column/dialog-filter-column.component';
import { DialogSortColumnComponent } from '@shared/component/dialog-sort-column/dialog-sort-column.component';
import { DialogArrangeColumnComponent } from '@shared/component/dialog-arrange-column/dialog-arrange-column.component';
import { WebInfo } from '@core/models/web-info';
import { DialogReturnLogComponent } from '@shared/component/dialog-return-log/dialog-return-log.component';
import { Sort } from '@core/models/sort';
import { Filter } from '@core/models/filter';
import { DialogDisplayArrangeColumnComponent } from '@shared/component/dialog-display-arrange-column/dialog-display-arrange-column.component';
import { DialogSaveArrangeColumnComponent } from '@shared/component/dialog-save-arrange-column/dialog-save-arrange-column.component';
import { DialogUpdateArrangeColumnComponent } from '@shared/component/dialog-update-arrange-column/dialog-update-arrange-column.component';
import { ArrangeService } from '@core/services/arrange/arrange.service';

@Component({
  selector: 'app-table-return-reverse-invoice',
  templateUrl: './table-return-reverse-invoice.component.html',
  styleUrls: ['./table-return-reverse-invoice.component.scss'],
})
export class TableReturnReverseInvoiceComponent implements OnChanges, OnInit {
  @Input() searchCriteria; // receive array list search criteria
  @Input() currentPageType; // receive page name
  @Input() nameComponent; // name of header for multiple header drilldown
  @Output() payBack: EventEmitter<any> = new EventEmitter<any>();

  // @ts-ignore
  @ViewChild(MatPaginator) paginator: MatPaginator;
  dataSource = new MatTableDataSource([]);
  selectionReversed = new SelectionModel<any>(true, []);
  tempSearchCriteria: any; // store temp for search criteria when back
  webInfo: WebInfo;
  listTempDataEdited: any;

  listData = [];
  currentPage = 1;
  gap = 5;
  columnsDisplay = [
    { key: 'isReverseInvoice', text: 'เลือก', sequence: 1, hide: false },
    { key: 'revInvoiceStatus', text: 'สถานะ', sequence: 2, hide: false },
    { key: 'reasonCode', text: 'เหตุผลในการกลับรายการ', sequence: 3, hide: false },
    { key: 'originalCompCode', text: 'รหัสหน่วยงาน', sequence: 4, hide: false },
    { key: 'originalDocumentNo', text: 'เลขที่เอกสาร', sequence: 5, hide: false },
    { key: 'invoiceLine', text: 'บรรทัดรายการ', sequence: 6, hide: false },
    { key: 'originalFiscalYear', text: 'ปีบัญชี', sequence: 7, hide: false },
    { key: 'paymentDate', text: 'วันที่ประมวลผล', sequence: 8, hide: false },
    { key: 'paymentName', text: 'การกำหนด', sequence: 9, hide: false },
    { key: 'vendor', text: 'ผู้ขาย', sequence: 10, hide: false },
    { key: 'paymentCompCode', text: 'รหัสบริษัทชำระเงิน', sequence: 11, hide: false },
    { key: 'paymentDocNo', text: 'เลขที่เอกสารการชำระเงิน', sequence: 12, hide: false },
    { key: 'paymentFiscalYear', text: 'ปีบัญชี', sequence: 13, hide: false },
    { key: 'transferDate', text: 'วันที่สั่งโอน', sequence: 14, hide: false },
    { key: 'fileName', text: 'ชื่อไฟล์', sequence: 15, hide: false },
  ];
  multiSort: Sort[] = [
    { id: 'refRunning', direction: 'ASC', sequence: 1 },
    { id: 'refLine', direction: 'DESC', sequence: 2 },
  ];
  filteredColumn: Filter[] = [];
  isSelectedAll = false;
  listValidate = [];
  arrangeId: number = null;
  defaultArrange: boolean = true;

  constructor(
    public constant: Constant,
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    public utils: Utils,
    public loaderService: LoaderService,
    private returnService: ReturnService,
    private snackBar: MatSnackBar,
    private localStorageService: LocalStorageService,
    private arrangeService: ArrangeService,
  ) {}

  ngOnInit() {
    this.defaultArrangeLastRow();
  }

  defaultArrangeLastRow() {
    if (this.defaultArrange) {
      this.arrangeService.findByArrangeCodeLastRow(this.nameComponent).then((response) => {
        if (!this.utils.isEmpty(response.data)) {
          if (response.data.length > 0) {
            this.arrangeId = response.data[0].id ? response.data[0].id : null;
            let arrangeJsonText = null;
            if (!this.utils.isEmpty(response.data[0].arrangeJsonText)) {
              arrangeJsonText = JSON.parse(response.data[0].arrangeJsonText);
            }
            this.columnsDisplay = [...arrangeJsonText];
          }
        }
      });
      this.defaultArrange = false;
    }
  }


  set(name1, name2) {
    return name2;
  }

  ngOnChanges(changes) {
    if (this.currentPageType === this.nameComponent) {
      this.webInfo = this.localStorageService.getWebInfo();
      this.tempSearchCriteria = Object.assign({}, this.searchCriteria);
      this.loadData();
      this.dataSource.paginator = this.paginator;
      this.clearForm();
    }
  }

  clearForm() {}

  getDisplayedColumns(): string[] {
    return this.columnsDisplay
      .filter((cd) => !cd.hide)
      .sort((a, b) => (a.sequence > b.sequence ? 1 : -1))
      .map((cd) => cd.key);
  }

  loadData() {
    this.returnService.getPropLogReverseDocInvoice(this.tempSearchCriteria).then((result) => {
      if (result.status === 200) {
        this.listData = [];
        this.dataSource = new MatTableDataSource(result.data);
        // this.dataSource.data.forEach((document) => {
        //   document.revInvoiceReason = '06';
        // });
        console.log(this.dataSource.data);
        this.dataSource.paginator = this.paginator;
        this.listTempDataEdited = JSON.parse(JSON.stringify(result.data));
      } else if (result.status === 403) {
      }
    });
  }

  sortColumn() {
    this.multiSort.sort((a, b) => (a.sequence > b.sequence ? 1 : -1));
    let sortedData = [];
    this.multiSort.forEach((sort) => {
      sortedData = this.dataSource.data.sort((a, b) => {
        if (sort.direction === 'ASC') {
          return a[sort.id] > b[sort.id] ? 1 : -1;
        } else if (sort.direction === 'DESC') {
          return a[sort.id] > b[sort.id] ? -1 : 1;
        } else {
          return 0;
        }
      });
    });
    this.dataSource.data = sortedData;
  }

  changeFileStatus(index, element) {
    const selectedItem = this.dataSource.data.find((e) => e.id === element.id);
    this.selectionReversed.toggle(element);
    selectedItem.revInvoiceStatus = !element.revInvoiceStatus;
  }

  validateFormRequest() {
    this.listValidate = [];
    this.dataSource.data.forEach((item) => {
      if (item.revInvoiceStatus) {
        if (item.reasonCode === null || item.reasonCode === '') {
          console.log('asdasdasd');
          this.listValidate.push(
            'กรุณาระบุเหตุผลการกลับรายการของรายการเลขที่เอกสาร : ' + item.originalDocumentNo
          );
        }
      }
    });
  }

  onSubmit() {
    const objRequest = [];
    this.validateFormRequest();

    if (this.listValidate.length === 0) {
      this.dataSource.data.forEach((item) => {
        console.log(item);

        // const reasonNoString = !item.revInvoiceReason.toString().startsWith('0') ? '0'.concat(item.revInvoiceReason) : item.revInvoiceReason;
        const reasonNoString = '06';
        console.log('reasonNoString', reasonNoString);
        if (item.revInvoiceStatus) {
          let revDateAcct = null;
          if (reasonNoString === '06') {
            console.log('in');
            revDateAcct = new Date(item.transferDate);
            const day = revDateAcct.getDate();
            const month = revDateAcct.getMonth() + 1;
            const year = revDateAcct.getFullYear();
            revDateAcct = this.utils.parseDate(day, month, year);
          } else {
            revDateAcct = null;
          }
          console.log(revDateAcct, revDateAcct);
          objRequest.push({
            id: item.id,
            accountDocNo: item.originalDocumentNo,
            compCode: item.originalCompCode,
            fiscalYear: item.originalFiscalYear,
            webInfo: this.webInfo,
            flag: 1,
            reason: item.reasonCode,
            // reasonNo: '06',
            reasonNo: reasonNoString,
            revDateAcct,
          });
        }
      });
      this.returnService.reverseInvoice(objRequest).then((result) => {
        if (result && result.status === 200) {
          const dialogRef = this.dialog.open(DialogReturnLogComponent, {
            width: '95vw',
            data: {
              listLog: result.data,
              logType: 'PAYMENT_INVOICE',
            },
          });
          dialogRef.afterClosed().subscribe((dialogResult) => {
            if (dialogResult && dialogResult.event) {
              this.loadData();
              const payBackObj = {
                type: 'BACK',
                step: '04',
              };
              this.payBack.emit(payBackObj);
            }
          });
        }
      });
    }
  }

  onBack() {
    const payBackObj = {
      type: 'BACK',
      step: '04',
    };
    this.payBack.emit(payBackObj);
  }

  openDialogReportFilterColumn() {
    // let columnNotFilter = this.columnTableAll.filter((n1) => {
    //   if (!this.columnCanNotFilter.includes(n1.key)) {
    //     return !this.filterList.some((n2) => {
    //       return n1.key === n2.key;
    //     });
    //   }
    // });
    const dialogRef = this.dialog.open(DialogFilterColumnComponent, {
      width: '90vw',
      data: {
        listAllColumn: this.columnsDisplay,
        filteredColumn: this.filteredColumn,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'Save') {
          const filteredList = result.value;
          if (filteredList.length > 0) {
            filteredList.forEach((item) => {
              this.filteredColumn.push({
                id: item.id,
                value: item.value,
              });
              this.tempSearchCriteria[item.id] = item.value;
            });
          }
          this.loadData();
        }
      }
    });
  }

  openDialogReportSortColumn() {
    const dialogRef = this.dialog.open(DialogSortColumnComponent, {
      width: '95vw',
      data: {
        listAllColumn: this.columnsDisplay,
        listColumnSorted: this.multiSort,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'Save') {
          this.multiSort = result.multiSort;
          this.sortColumn();
        }
      }
    });
  }

  openDialogReportArrangeColumn() {
    this.columnsDisplay = this.columnsDisplay.sort((a, b) => (a.sequence > b.sequence ? 1 : -1));
    const dialogRef = this.dialog.open(DialogArrangeColumnComponent, {
      width: '95vw',
      data: {
        listColumnShow: this.columnsDisplay,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'Save') {
          this.columnsDisplay = result.columnsDisplay;
          this.columnsDisplay = this.columnsDisplay.sort((a, b) =>
            a.sequence > b.sequence ? 1 : -1
          );
        }
      }
    });
  }

  checkStatus(id) {
    const item = this.listTempDataEdited.find((e) => e.id === id);
    console.log('revPaymentDocNo : ', item.revPaymentDocNo);
    if (item.revPaymentDocNo) {
      return 'icon-return-not-assign';
    } else {
      return 'icon-return-return';
    }
  }

  selectAll() {
    if (this.isSelectedAll) {
      this.dataSource.data.forEach((item) => {
        this.selectionReversed.deselect(item);
        const selectedItem = this.dataSource.data.find((e) => e.id === item.id);
        selectedItem.revInvoiceStatus = false;
      });
    } else {
      this.dataSource.data.forEach((item) => {
        this.selectionReversed.select(item);
        const selectedItem = this.dataSource.data.find((e) => e.id === item.id);
        selectedItem.revInvoiceStatus = true;
      });
    }
    this.isSelectedAll = !this.isSelectedAll;
  }

  patchValueReason(event, element) {
    const selectedItem = this.dataSource.data.find((e) => e.id === element.id);
    selectedItem.reasonCode = event.target.value;
  }

  viewDocument(item, type) {
    if (type === 'invoice') {
      const url =
        './detail-fi-kb?compCode=' +
        item.originalCompCode +
        '&docNo=' +
        item.originalDocumentNo +
        '&docYear=' +
        item.originalFiscalYear;
      window.open(url, 'name', 'width=1200,height=700');
    } else {
      const url =
        './detail-fi-kb?compCode=' +
        item.paymentCompCode +
        '&docNo=' +
        item.paymentDocNo +
        '&docYear=' +
        item.paymentFiscalYear;
      window.open(url, 'name', 'width=1200,height=700');
    }
  }

  openDialogReportDisplayArrangeColumn() {
    const dialogRef = this.dialog.open(DialogDisplayArrangeColumnComponent, {
      width: '95vw',
      data: {
        listColumnShow: this.columnsDisplay,
        type: this.nameComponent,
        arrangeId: this.arrangeId,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'success') {
          this.arrangeId = result.id ? result.id : null;
          this.columnsDisplay = [...result.arrangeJsonText];
        }
      }
    });
  }

  openDialogReportSaveArrangeColumn() {
    const dialogRef = this.dialog.open(DialogSaveArrangeColumnComponent, {
      width: '50vw',
      data: {
        listColumnShow: this.columnsDisplay,
        type: this.nameComponent,
        arrangeId: this.arrangeId,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'success') {
          this.arrangeId = result.data.id;
          this.columnsDisplay = [...result.data.arrangeJsonText];
        }
      }
    });
  }

  openDialogReportUpdateArrangeColumn() {
    const dialogRef = this.dialog.open(DialogUpdateArrangeColumnComponent, {
      width: '50vw',
      data: {
        listColumnShow: this.columnsDisplay,
        type: this.nameComponent,
        arrangeId: this.arrangeId,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'success') {
          this.arrangeId = result.data.id;
          this.columnsDisplay = [...result.data.arrangeJsonText];
        }
      }
    });
  }
}
