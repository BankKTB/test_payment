import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material';
import { MatPaginator } from '@angular/material/paginator';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss'],
})
export class ReportComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  panelExpanded = true;
  displayedColumns: string[] = [
    'Comp',
    'Year',
    'DocNumber',
    'Vendor',
    'InvoiceDoc',
    'Year1',
    'Message',
  ];
  displayedColumns1: string[] = [
    'Date',
    'Time',
    'Code',
    'NoDoc',
    'Year',
    'List',
    'OldList',
    'NewList',
    'User',
    'SyUser',
  ];
  dataSource = new MatTableDataSource([
    {
      Comp: '1505',
      Year: '2008',
      DocNumber: '3600000284',
      Vendor: '1000000006',
      InvoiceDoc: 'null',
      Year1: 'null',
      Message: 'ไม่มีการป้อนข้อมูลนำเข้าแบบสำหรับจอภาพ',
    },
    {
      Comp: '1505',
      Year: '2008',
      DocNumber: '3600000284',
      Vendor: '1000000006',
      InvoiceDoc: 'null',
      Year1: 'null',
      Message: 'งวดการผ่านรายการ 003 2008 ไม่เปิด',
    },
    {
      Comp: '1505',
      Year: '2008',
      DocNumber: '3600000284',
      Vendor: '1000000006',
      InvoiceDoc: 'null',
      Year1: 'null',
      Message: 'ไม่มีการป้อนข้อมูลขาเข้าสำหรับจอภาพ SAPMF05L 0303',
    },
    {
      Comp: '1505',
      Year: '2008',
      DocNumber: '3600000284',
      Vendor: '',
      InvoiceDoc: 'null',
      Year1: 'null',
      Message: 'เงินในบัญชีไม่พอจ่าย line chk (K)',
    },
  ]);
  dataSource1 = new MatTableDataSource([
    {
      Date: 1578330000000,
      Time: 1578330000000,
      Code: '1505',
      NoDoc: '3600000284',
      Year: '2008',
      List: '001',
      OldList: 'B',
      NewList: 'E',
      User: null,
      SyUser: 'GFQA903',
    },
  ]);

  constructor() {}

  ngOnInit() {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }
}
