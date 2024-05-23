import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LoaderService {
  isLoading: boolean;
  loadingVisableChange: Subject<boolean> = new BehaviorSubject<boolean>(false);

  constructor() {
    this.loadingVisableChange.subscribe((value) => {
      setTimeout(() => (this.isLoading = value), 0);
    });
  }

  loadingToggleStatus(flag) {
    this.loadingVisableChange.next(flag);
  }
}
