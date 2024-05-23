import { Component, HostListener, OnInit } from '@angular/core';

@Component({
  selector: 'app-back-to-top',
  templateUrl: './back-to-top.component.html',
  styleUrls: ['./back-to-top.component.scss'],
})
export class BackToTopComponent implements OnInit {
  isHideToTop: boolean = true;
  isHideToBottom: boolean = true;

  constructor() {
  }

  ngOnInit() {
    this.isHideToBottom = !((document.body.clientHeight) <= document.body.scrollHeight);
  }

  @HostListener('window:scroll', ['$event']) // for window scroll events
  onScroll(event) {
    if ((document.body.clientHeight) <= document.body.scrollHeight) {
      if (window.scrollY < (document.body.clientHeight - 500)) {
        this.isHideToTop = true;
      }
      if (window.scrollY > 100) {
        this.isHideToTop = false;
      }
      if ((document.body.clientHeight + window.scrollY + 100) >= document.body.scrollHeight) {
        this.isHideToBottom = true;
      }
      if ((document.body.clientHeight + window.scrollY + 100) <= document.body.scrollHeight) {
        this.isHideToBottom = false;
      }
    } else {
      // this.isHideToBottom = true
    }


    // console.log('onScroll', event, sct.body.scrollTop);
  }

  scrollToTop() {
    window.scroll({
      top: 0,
      left: 0,
      behavior: 'smooth'
    });
  }

  scrollToBottom() {
    window.scroll({
      top: document.body.scrollHeight,
      left: 0,
      behavior: 'smooth'
    });
  }
}
