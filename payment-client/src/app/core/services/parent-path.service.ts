import { Injectable } from '@angular/core';

import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ParentPathService {
  nowPage: string;

  parentPath: Subject<string> = new Subject<string>();

  constructor() {
    this.parentPath.subscribe((value) => {
      this.nowPage = value;
    });
  }

  updateParentPath(nowPage) {
    this.parentPath.next(nowPage);
  }
}
