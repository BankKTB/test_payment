import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnInit,
  Output,
  QueryList,
  ViewChildren,
} from '@angular/core';
import { MatDialog } from '@angular/material';
import { FormBuilder } from '@angular/forms';
import { Utils } from '@shared/utils/utils';
import { DialogSearchFieldComponent } from '@shared/component/dialog-additional/dialog-search-field/dialog-search-field.component';
import { DialogCriteriaComponent } from '@shared/component/dialog-criteria/dialog-criteria.component';

export interface Food {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-tab-independent',
  templateUrl: './tab-independent.component.html',
  styleUrls: ['./tab-independent.component.scss'],
})
export class TabIndependentComponent implements OnInit {
  @ViewChildren('fieldName') fieldName: QueryList<ElementRef>;
  @ViewChildren('conditionField') conditionField: QueryList<ElementRef>;
  @ViewChildren('optionExclude') optionExclude: QueryList<ElementRef>;

  @Input() independent;
  @Output() messageFromIndependent = new EventEmitter<any>();

  // Tab
  panelExpanded = true;

  listIndependent = [
    {
      fieldName: null,
      conditionField: '',
      optionExclude: false,
      dataType: 'STRING',
      dbName: null,
      tableName: null,
      condition: [],
    },
    {
      fieldName: null,
      conditionField: '',
      optionExclude: false,
      dataType: 'STRING',
      dbName: null,
      tableName: null,
      condition: [],
    },
    {
      fieldName: null,
      conditionField: '',
      optionExclude: false,
      dataType: 'STRING',
      dbName: null,
      tableName: null,
      condition: [],
    },
  ];

  constructor(private dialog: MatDialog, private formBuilder: FormBuilder, private utils: Utils) {}

  ngOnInit() {
    console.log('ngOnInit tab indepentdent');
    // if (this.independent) {
    //   this.independent.forEach(item => {
    //     console.log(item)
    //     if (item.conditionField && item.conditionField !== '') {
    //       item.conditionField = this.utils.convertConditionFieldArrayToConditionFieldText(item.conditionField)
    //     }
    //   })
    //   this.listIndependent = this.independent
    // }
  }

  defaultIndependet() {
    this.listIndependent = [
      {
        fieldName: null,
        conditionField: '',
        optionExclude: false,
        dataType: 'STRING',
        dbName: null,
        tableName: null,
        condition: [],
      },
      {
        fieldName: null,
        conditionField: '',
        optionExclude: false,
        dataType: 'STRING',
        dbName: null,
        tableName: null,
        condition: [],
      },
      {
        fieldName: null,
        conditionField: '',
        optionExclude: false,
        dataType: 'STRING',
        dbName: null,
        tableName: null,
        condition: [],
      },
    ];
  }

  getIndependentFromCopy(object) {
    object.forEach((item) => {
      if (item.conditionField && item.conditionField !== '') {
        item.conditionField = this.utils.convertConditionFieldArrayToConditionFieldText(
          item.condition
        );
      }
    });
    this.listIndependent = object;
  }

  updateParameter(): void {
    this.listIndependent.forEach((item) => {
      if (item.conditionField && item.conditionField !== '') {
        if (
          item.dbName === 'DATE_ACCT' ||
          item.dbName === 'DATE_DOC' ||
          item.dbName === 'DATE_BASE_LINE' ||
          item.dbName === 'DOCUMENT_CREATED'
        ) {
          const oldFieldList = item.conditionField.split(',');
          let newConditionField = '';
          oldFieldList.forEach((fieldValue, index) => {
            if (index !== 0) {
              newConditionField = newConditionField + ',';
            }
            if (fieldValue.indexOf('-') > -1) {
              const fieldValueRange = fieldValue.split('-');
              const fieldFrom = fieldValueRange[0];
              const fieldTo = fieldValueRange[1];
              const yearFrom = fieldFrom.substring(0, 4);
              const monthFrom = fieldFrom.substring(4, 6);
              const dayFrom = fieldFrom.substring(6, 8);
              const yearTo = fieldTo.substring(0, 4);
              const monthTo = fieldTo.substring(4, 6);
              const dayTo = fieldTo.substring(6, 8);
              const fieldDateFrom = new Date(
                (yearFrom + '-' + monthFrom + '-' + dayFrom).toString()
              );
              const fieldDateTo = new Date((yearTo + '-' + monthTo + '-' + dayTo).toString());
              let indexInRange = 0;
              for (const d = fieldDateFrom; d <= fieldDateTo; d.setDate(d.getDate() + 1)) {
                if (indexInRange !== 0) {
                  newConditionField = newConditionField + ',';
                }
                newConditionField =
                  newConditionField +
                  this.utils.parseDateForParameter(d.getDate(), d.getMonth() + 1, d.getFullYear());
                indexInRange++;
              }
            } else {
              newConditionField = newConditionField + fieldValue;
            }
          });
          item.condition = this.utils.convertConditionFieldTextToConditionFieldArrayHaveExclude(
            newConditionField
          );
        } else {
          item.condition = this.utils.convertConditionFieldTextToConditionFieldArray(
            item.conditionField
          );
        }
      } else {
        item.condition = this.utils.convertConditionFieldTextToConditionFieldArray(
          item.conditionField
        );
      }
    });
    this.independent = this.listIndependent;
    this.messageFromIndependent.emit(this.independent);
  }

  setConditionfield(index) {
    const conditionField = this.conditionField.toArray()[index].nativeElement.value;
    console.log(conditionField);
    if (conditionField) {
      console.log('in');
      this.listIndependent[index].conditionField = conditionField;
    } else {
      console.log('out');
      this.listIndependent[index].conditionField = '';
    }
  }

  setConditionDateField(event, index) {
    const date: Date = event.target.value;
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();
    this.listIndependent[index].conditionField = this.utils.parseDateForParameter(day, month, year);
  }

  setOptionInclude(index) {
    const valueIndex = this.optionExclude.toArray()[index] as any;

    const optionExclude = valueIndex._checked;
    if (optionExclude) {
      this.listIndependent[index].optionExclude = optionExclude;
    } else {
      this.listIndependent[index].optionExclude = false;
    }
  }

  addInputIndependent() {
    this.listIndependent.push({
      fieldName: null,
      conditionField: '',
      optionExclude: false,
      dataType: 'STRING',
      dbName: null,
      tableName: null,
      condition: [],
    });
  }

  deleteInputIndependent(index) {
    this.listIndependent.splice(index, 1);
  }

  openDialogSearchfield(index) {
    const dialogRef = this.dialog.open(DialogSearchFieldComponent, {});
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        console.log(result);
        this.listIndependent[index].fieldName = result.fieldName;
        this.listIndependent[index].dataType = result.dataType.toUpperCase();
        this.listIndependent[index].dbName = result.dbName;
        this.listIndependent[index].tableName = result.tableName;
      }
      console.log(this.listIndependent);
    });
  }

  openDialogDateParameter(index) {
    const oldField = this.listIndependent[index].conditionField;
    const oldFieldList = oldField.split(',');
    let listCriteria = [];
    let flag = false;
    if (oldFieldList.length) {
      let from = null;
      let to = null;
      let exclude = false;
      oldFieldList.forEach((fieldValue) => {
        let yearFrom = '';
        let monthFrom = '';
        let dayFrom = '';
        let yearTo = '';
        let monthTo = '';
        let dayTo = '';
        if (flag) {
          if (fieldValue.endsWith(')')) {
            yearTo = fieldValue.substring(0, 4);
            monthTo = fieldValue.substring(4, 6);
            dayTo = fieldValue.substring(6, 8);
            flag = false;
            to = monthTo + '-' + dayTo + '-' + yearTo;
          } else {
            from = '';
            to = '';
            flag = false;
          }
        } else {
          if (fieldValue.startsWith('(')) {
            yearFrom = fieldValue.substring(1, 5);
            monthFrom = fieldValue.substring(5, 7);
            dayFrom = fieldValue.substring(7, 9);
            flag = true;
            from = monthFrom + '-' + dayFrom + '-' + yearFrom;
          } else {
            yearFrom = fieldValue.substring(0, 4);
            monthFrom = fieldValue.substring(4, 6);
            dayFrom = fieldValue.substring(6, 8);
            from = monthFrom + '-' + dayFrom + '-' + yearFrom;
            to = '';
            flag = false;
          }
        }
        if (!flag) {
          listCriteria.push({
            from: from,
            to: to,
            exclude: exclude,
          });
        }
      });
    }
    const dialogRef = this.dialog.open(DialogCriteriaComponent, {
      width: '60vw',
      data: {
        listCriteria: listCriteria,
        typeParam: 'LIST',
        keyColumn: 'key',
        typeInput: 'DATE',
        thaiText: 'วันผ่านรายการ',
        enableExclude: false,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'save') {
          const values = result.value;
          if (values.length > 0) {
            let appendString = '';
            values.forEach((value, index) => {
              if (index !== 0) {
                appendString = appendString + ',';
              }
              if (!this.utils.isEmpty(value.from) && !this.utils.isEmpty(value.to)) {
                const dateFrom: Date = new Date(value.from);
                const dateTo: Date = new Date(value.to);
                const yearFrom = dateFrom.getFullYear();
                const monthFrom = dateFrom.getMonth() + 1;
                const dayFrom = dateFrom.getDate();
                const yearTo = dateTo.getFullYear();
                const monthTo = dateTo.getMonth() + 1;
                const dayTo = dateTo.getDate();
                let string =
                  this.utils.parseDateForParameter(
                    dayFrom.toString(),
                    monthFrom.toString(),
                    yearFrom.toString()
                  ) +
                  ',' +
                  this.utils.parseDateForParameter(
                    dayTo.toString(),
                    monthTo.toString(),
                    yearTo.toString()
                  );
                appendString = appendString + '(' + string + ')';
              } else if (!this.utils.isEmpty(value.from)) {
                const dateFrom: Date = new Date(value.from);
                const yearFrom = dateFrom.getFullYear();
                const monthFrom = dateFrom.getMonth() + 1;
                const dayFrom = dateFrom.getDate();
                let string = this.utils.parseDateForParameter(
                  dayFrom.toString(),
                  monthFrom.toString(),
                  yearFrom.toString()
                );
                appendString = appendString + string;
              }
            });
            this.listIndependent[index].conditionField = appendString;
          }
        }
      }
    });
  }
}
