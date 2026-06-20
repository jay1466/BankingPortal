import { Component, OnInit, Input } from '@angular/core';
import { ApiService } from 'src/app/services/api.service';
import { TransactionComponent } from '../transaction/transaction.component';
import { EMPTY, catchError, tap } from 'rxjs';

@Component({
  selector: 'app-transaction-history',
  templateUrl: './transaction-history.component.html',
  styleUrls: ['./transaction-history.component.css'],
})
export class TransactionHistoryComponent implements OnInit {
  transactionHistory: any[] = [];
  filteredTransactionHistory: any[] = [];
  userAccountNumber: string | null = null;
  
  // UI State
  loading: boolean = true;
  filterType: string = 'ALL';
  searchId: string = '';
  currentPage: number = 0;
  pageSize: number = 10;
  
  // Helpers for template
  abs = Math.abs;
  Math = Math;
  @Input() isStandalone: boolean = true;

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadTransactionHistory();
  }

  loadTransactionHistory(): void {
    this.loading = true;
    this.userAccountNumber = TransactionComponent.getAccountNumberFromToken();

    this.apiService
      .getTransactions()
      .pipe(
        tap((data) => {
          this.transactionHistory = data.map((t: any) => {
             let mappedType = '';
             let isCredit = false;
             
             if (t.transactionType === 'CASH_DEPOSIT') {
                 mappedType = 'Deposit';
                 isCredit = true;
             } else if (t.transactionType === 'CASH_WITHDRAWAL') {
                 mappedType = 'Withdrawal';
                 isCredit = false;
             } else if (t.transactionType === 'CASH_TRANSFER') {
                 mappedType = 'Transfer';
                 isCredit = t.targetAccountNumber === this.userAccountNumber;
             }
             
             return { ...t, uiType: mappedType, isCredit };
          });
          this.filterTransactions();
          this.loading = false;
        }),
        catchError((error) => {
          console.error('Error fetching transaction history:', error);
          this.loading = false;
          return EMPTY;
        })
      )
      .subscribe();
  }

  filterTransactions(): void {
    let filtered = [...this.transactionHistory];

    if (this.filterType === 'DEPOSIT') {
      filtered = filtered.filter(t => t.uiType === 'Deposit');
    } else if (this.filterType === 'WITHDRAWAL') {
      filtered = filtered.filter(t => t.uiType === 'Withdrawal');
    } else if (this.filterType === 'FUND_TRANSFER') {
      filtered = filtered.filter(t => t.uiType === 'Transfer');
    }

    if (this.searchId && this.searchId.trim() !== '') {
      filtered = filtered.filter(t => 
        t.transactionId && t.transactionId.toLowerCase().includes(this.searchId.toLowerCase())
      );
    }

    this.filteredTransactionHistory = filtered;
    this.currentPage = 0; // Reset to first page
  }

  onFilterChange(): void {
    this.filterTransactions();
  }

  onSearchChange(): void {
    this.filterTransactions();
  }

  get transactions(): any[] {
    const startIndex = this.currentPage * this.pageSize;
    return this.filteredTransactionHistory.slice(startIndex, startIndex + this.pageSize);
  }

  get totalPages(): number {
    return Math.max(1, Math.ceil(this.filteredTransactionHistory.length / this.pageSize));
  }

  changePage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
    }
  }

  downloadStatement(): void {
    // Placeholder for email/download statement functionality
    alert('Statement export feature coming soon!');
  }
}
