import { ThaidatePipe } from '@shared/pipe/thaidate.pipe';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialogRef } from '@angular/material/dialog';
import { PaymentAliasService } from '@core/services/payment-alias/payment-alias.service';

@Component({
  selector: 'app-dialog-search-parameter',
  templateUrl: './dialog-search-parameter.component.html',
  styleUrls: ['./dialog-search-parameter.component.scss'],
})
export class DialogSearchParameterComponent implements OnInit, AfterViewInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  displayedColumns: string[] = ['choose', 'paymentDate', 'paymentName', 'statusName'];

  dataSource = new MatTableDataSource([]);

  pipe: ThaidatePipe;
  errorMessage = '';

  constructor(
    private dialogRef: MatDialogRef<DialogSearchParameterComponent>,
    private paymentAliasService: PaymentAliasService
  ) {}

  closeDialog(): void {
    const dialogRef = this.dialogRef.close();
  }

  ngOnInit() {}

  ngAfterViewInit(): void {
    this.searchAllParameter();
  }

  chooseDataSearch(data) {
    console.log(data);
    // this.errorMessage = '';
    this.dialogRef.close({
      event: true,
      // type: this.data.type,
      paymentDate: data.paymentDate,
      paymentName: data.paymentName,
    });
  }

  searchAllParameter() {
    this.paymentAliasService.searchByValue('**').then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.dataSource = new MatTableDataSource(data);
          this.dataSource.sort = this.sort;
          this.setDateThaiForFilter();
        }
      } else if (result.status === 404) {
      }
    });
  }

  searchByValue(event) {
    const textSearch = event.value;
    console.log('searchByValue');
    console.log(textSearch);
    console.log(this.checkStarAndPercent(textSearch));
    if (textSearch !== '') {
      this.paymentAliasService
        .searchByValue(this.checkStarAndPercent(textSearch))
        .then((result) => {
          console.log(result);
          if (result.status === 200) {
            const data = result.data;
            if (data) {
              this.dataSource = new MatTableDataSource(data);
              this.dataSource.sort = this.sort;
              this.setDateThaiForFilter();
            }
          } else if (result.status === 404) {
          }
        });
    } else {
      this.searchAllParameter();
    }
  }

  applyFilter(filterValue: string) {
    console.log(filterValue);
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  setDateThaiForFilter() {
    this.pipe = new ThaidatePipe();
    // console.log('hi', this.dataSource.filterPredicate);
    const defaultPredicate = this.dataSource.filterPredicate;
    this.dataSource.filterPredicate = (data, filter) => {
      const formatted = this.pipe.transform(data.paymentDate, '');
      return formatted.indexOf(filter) >= 0 || defaultPredicate(data, filter);
    };
  }

  checkStarAndPercent(textSearch) {
    const slash = textSearch.split('').filter((value) => value === '/').length;
    if (slash > 0) {
      for (let i = 0; i < slash; i++) {
        textSearch = textSearch.replace('/', '');
      }
    }
    const percent = textSearch.split('').filter((value) => value === '%').length;
    if (percent > 0) {
      for (let i = 0; i < percent; i++) {
        textSearch = textSearch.replace('%', '*');
      }
      const textStar = textSearch.split('').filter((value) => value === '*').length;
      if (textStar > 2) {
        this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
        return 'error';
      } else {
        const checkCondtion = textSearch.indexOf('**');
        if (checkCondtion === -1) {
          if (textSearch === '*' || textSearch === '**') {
            this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
            return 'error';
          }
        } else {
          this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
          return 'error';
        }
      }
    } else {
      const textStar = textSearch.split('').filter((value) => value === '*').length;
      if (textStar === 0) {
        textSearch = '*' + textSearch + '*';
      }
    }
    return textSearch;
  }
}
