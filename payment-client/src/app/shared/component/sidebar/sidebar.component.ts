import { UserProfile } from '@core/models/user-profile';
import { LocalStorageService } from '@core/services/local-storage.service';
import { Component, OnInit } from '@angular/core';

import { MatDialog } from '@angular/material/dialog';

import { Router } from '@angular/router';
import { SidebarService } from '@core/services/sidebar.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent implements OnInit {
  opened: boolean;
  isLogin = false;
  userProfile: UserProfile;
  listSidebar = [];
  headmenu = 'เลือกรายการที่ต้องการ';

  isSidebarVisible = true;
  menuClassMobile = '';
  pageType = '';
  pageName = '';
  activeIndex = 0;
  // maxIndexMenu = 13; // set max index menu
  maxIndexMenu = 5; // set max index menu
  constructor(
    private router: Router,
    private localStorageService: LocalStorageService,
    private sidebarService: SidebarService,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.userProfile = this.localStorageService.getUserProfile();

    this.sidebarService.sidebarVisibilityChange.subscribe((value) => {
      this.isSidebarVisible = this.sidebarService.isSidebarVisible;
    });

    this.sidebarService.sidebarUserProfileChange.subscribe((value) => {
      this.userProfile = this.sidebarService.userProfile;
    });

    this.sidebarService.sidebarNowPageChange.subscribe((value) => {
      this.pageName = this.sidebarService.nowPage;
      this.pageType = this.sidebarService.pageType;
      console.log(this.pageName);
      console.log(this.pageType);
      this.setMenu();
    });

    this.sidebarService.sidebarToggleChange.subscribe((value) => {
      console.log(value);
      this.menuClassMobile = value ? 'opened' : '';
    });
  }

  logout() {
    this.userProfile = null;
    localStorage.removeItem('userProfile');
    localStorage.removeItem('userMatrix');
    localStorage.removeItem('webInfo');
    this.router.navigate(['/']);
  }

  setMenu() {
    this.activeIndex = 0;
    this.mapMenu(this.pageType);
    console.log(this.pageType);
  }

  changeActiveMenu() {
    if (this.activeIndex >= this.maxIndexMenu) {
      this.activeIndex = 0;
    } else {
      this.activeIndex += 1;
    }
    this.mapMenuWithActiveIndex(this.activeIndex);
  }

  mapMenuWithActiveIndex(activeIndex) {
    this.headmenu = 'เลือกรายการที่ต้องการ';
    this.listSidebar = [];
    if (activeIndex === 0) {
      this.mapMenu('');
    } else if (activeIndex === 1) {
      this.mapMenu('om');
    } else if (activeIndex === 2) {
      this.mapMenu('payment');
    } else if (activeIndex === 3) {
      this.mapMenu('reverse');
    } else if (activeIndex === 4) {
      this.mapMenu('generate');
    } else if (activeIndex === 5) {
      this.mapMenu('config');
    } else if (activeIndex === 6) {
      this.mapMenu('view');
    } else if (activeIndex === 7) {
      this.mapMenu('ju');
    } else if (activeIndex === 8) {
      this.mapMenu('change');
    }
  }

  mapMenu(type) {
    this.headmenu = '';
    this.listSidebar = [];
    switch (type) {
      case '':
        this.headmenu = 'เลือกรายการที่ต้องการ';
        break;
      case 'om':
        this.headmenu = 'ระบบอนุมัติเอกสารขอเบิก';
        this.listSidebar.push({
          link: '/omCgd',
          name: 'อนุมัติ',
          detail: 'CGD - การอนุมัติจ่ายเงินโดยกรมบัญชีกลาง',
          active: this.pageName === 'omCgd' ? true : false,
          data: 'AP01',
        });
        this.listSidebar.push({
          link: '/omPto',
          name: 'อนุมัติ',
          detail: 'PTO - การอนุมัติจ่ายเงินโดยกรมบัญชีกลาง/คลังจังหวัด',
          active: this.pageName === 'omPto' ? true : false,
          data: 'AP02',
        });
        this.listSidebar.push({
          link: '/omPtoH',
          name: 'อนุมัติ',
          detail: 'PTO_H - การอนุมัติจ่ายค่ารักษาพยาบาลโดยกรมบัญชีกลาง',
          active: this.pageName === 'omPtoH' ? true : false,
          data: 'AP03',
        });
        // this.listSidebar.push({
        //   link: '/omLog',
        //   name: 'รายงาน',
        //   detail: 'รายงานผลการอนุมัติ',
        //   active: this.pageName === 'omLog' ? true : false,
        //   data: 'AP04',
        // });
        // this.listSidebar.push({
        //   link: '/om-result',
        //   name: 'รายงาน',
        //   detail: 'รายละเอียดการอนุมัติเอกสารภายในวัน B เป็น ว่าง',
        //   active: this.pageName === 'om-result' ? true : false,
        //   data: 'AP05',
        // });
        // this.listSidebar.push({
        //   link: '/om-result-e',
        //   name: 'รายงาน',
        //   detail: 'รายละเอียดการอนุมัติเอกสารภายในวัน B เป็น E',
        //   active: this.pageName === 'om-result-e' ? true : false,
        //   data: 'AP06',
        // });
        // this.listSidebar.push({
        //   link: '/om-result-summary',
        //   name: 'รายงาน',
        //   detail: 'สรุปรายละเอียดการอนุมัติเอกสารภายในวัน B เป็น ว่าง',
        //   active: this.pageName === 'om-result-summary' ? true : false,
        //   data: 'AP07',
        // });
        // this.listSidebar.push({
        //   link: '/om-result-e-summary',
        //   name: 'รายงาน',
        //   detail: 'สรุปรายละเอียดการอนุมัติเอกสารภายในวัน B เป็น E',
        //   active: this.pageName === 'om-result-e-summary' ? true : false,
        //   data: 'AP08',
        // });
        break;
      case 'payment':
        this.headmenu = 'ระบบประมวลผลจ่าย';
        this.listSidebar.push({
          link: '/payment',
          name: 'ประมวลผลจ่าย',
          detail: '',
          active: this.pageName === 'payment' ? true : false,
          data: 'PM01',
        });
        this.listSidebar.push({
          link: '/selectGroupDocument',
          name: 'โปรแกรมการเลือกเอกสารเพื่อ Run payment',
          detail: '',
          active: this.pageName === 'selectGroupDocument' ? true : false,
          data: 'PM02',
        });
        this.listSidebar.push({
          link: '/batch-job/search',
          name: 'ค้นหา Payment Job Monitoring',
          detail: '',
          active: this.pageName === 'batch-job-search',
          data: 'PM03',
        });
        this.listSidebar.push({
          link: '/batch-job/create',
          name: 'สร้าง Payment Job Monitoring',
          detail: '',
          active: this.pageName === 'batch-job-create',
          data: 'PM04',
        });
        break;
      case 'generate':
        this.headmenu = 'ระบบสร้างไฟล์';
        this.listSidebar.push({
          link: '/generate',
          name: 'สร้างไฟล์',
          detail: '',
          active: this.pageName === 'generate' ? true : false,
          data: 'CF01',
        });
        // this.listSidebar.push({
        //   link: '/generateReport',
        //   name: 'รายงานการสร้างไฟล์',
        //   detail: '',
        //   active: this.pageName === 'generateReport' ? true : false,
        //   data: 'CF02',
        // });
        this.listSidebar.push({
          link: '/return',
          name: 'กลับรายการเอกสารและกำหนดรายการคืนกลับ',
          detail: '',
          active: this.pageName === 'return' ? true : false,
          data: 'KJ03',
        });
        this.listSidebar.push({
          link: '/proposal-tr1',
          name: 'สรุปยอดรายจ่ายรวมจาก TR1',
          detail: '',
          active: this.pageName === 'proposal-tr1',
          data: 'TR1',
        });
        break;
      case 'config':
        this.headmenu = 'ตั้งค่า';
        // this.listSidebar.push({
        //   link: '/companyPayee',
        //   name: 'รหัสบริษัท',
        //   detail: '',
        //   active: false,
        // });
        // this.listSidebar.push({
        //   link: '/companyPaying',
        //   name: 'รหัสบริษัทชำระ',
        //   detail: '',
        //   active: false,
        // });
        // this.listSidebar.push({
        //   link: '/payMethod',
        //   name: 'วิธีชำระเงิน/ประเทศ',
        //   detail: '',
        //   active: false,
        // });
        this.listSidebar.push({
          link: '/smartFee',
          name: 'ค่าธรรมเนียม SMART',
          detail: '',
          active: false,
          data: 'SS04',
        });
        this.listSidebar.push({
          link: '/swiftFee',
          name: 'ค่าธรรมเนียม SWIFT',
          detail: '',
          active: false,
          data: 'SS05',
        });
        this.listSidebar.push({
          link: '/sumFileCondition',
          name: 'ตารางกำหนดเงื่อนไขการรวมยอดโอนเงิน',
          detail: '',
          active: false,
          data: 'SS06',
        });
        this.listSidebar.push({
          link: '/holiday/create',
          name: 'กำหนดวันหยุด',
          detail: '',
          active: false,
          data: 'SS11',
        });
        break;
      case 'view':
        this.headmenu = 'แสดงค่าของระบบ';
        this.listSidebar.push({
          link: '/view-smartFee',
          name: 'ค่าธรรมเนียม SMART',
          detail: '',
          active: false,
          data: 'SS07',
        });
        this.listSidebar.push({
          link: '/view-swiftFee',
          name: 'ค่าธรรมเนียม SWIFT',
          detail: '',
          active: false,
          data: 'SS08',
        });
        this.listSidebar.push({
          link: '/view-sumFileCondition',
          name: 'ตารางกำหนดเงื่อนไขการรวมยอดโอนเงิน',
          detail: '',
          active: false,
          data: 'SS09',
        });
        this.listSidebar.push({
          link: '/holiday/search',
          name: 'แสดงวันหยุด',
          detail: '',
          active: false,
          data: 'SS12',
        });
        this.listSidebar.push({
          link: '/report-duplicate-pay',
          name: 'แสดงรายการจ่ายซ้ำ',
          detail: '',
          active: false,
          data: 'DUPPAY',
        });
        break;
      case 'reverse':
        this.headmenu = 'กลับรายการเอกสาร';
        this.listSidebar.push({
          link: '/reverse/payment',
          name: 'กลับรายการเอกสารจ่าย',
          active: this.pageName === 'reverse-payment' ? true : false,
          data: 'KJ01',
        });
        this.listSidebar.push({
          link: '/reverse/invoice',
          name: 'กลับรายการเอกสารตั้งเบิก',
          active: this.pageName === 'reverse-invoice' ? true : false,
          data: 'KJ02',
        });
        break;
      case 'ju':
        this.headmenu = 'รายการจ่ายเงินจากบัญชีเงินคลัง';
        this.listSidebar.push({
          link: '/generate-ju/search',
          name: 'รายการจ่ายเงินจากบัญชีเงินคลัง',
          active: this.pageName === 'ju' ? true : false,
          data: 'JU01',
        });
        break;
      case 'change':
        this.headmenu = 'การเปลี่ยนแปลงบรรทัดรายการ';
        this.listSidebar.push({
          link: '/change-document',
          name: 'การเปลี่ยนแปลงบรรทัดรายการ',
          active: this.pageName === 'change-document' ? true : false,
          // data: 'CB01',
        });

        break;
      default:
        break;
    }
    console.log(this.listSidebar);
  }

  linkPage(item) {
    console.log(item.link);
    if (this.sidebarService.nowPage !== item.link.substring(1, item.link.length)) {
      this.router.navigate([item.link]);
    }
  }

  mainPage() {
    this.router.navigate(['/menu']);
  }
}
