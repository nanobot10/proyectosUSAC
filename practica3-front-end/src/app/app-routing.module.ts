import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component';
import { AuthGuardService } from './services/auth-guard.service';
import { SignUpComponent } from './pages/sign-up/sign-up.component';
import { TransactionComponent } from './pages/transaction/transaction.component';
import { CreditComponent } from './pages/credit/credit.component';
import { DebitComponent } from './pages/debit/debit.component';
import { BalanceComponent } from './pages/balance/balance.component';


const routes: Routes = [
  {
    path: 'login', component: LoginComponent,
  },
  {
    path: 'signup', component: SignUpComponent,
  },
  {
    path: 'home', component: HomeComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'transaction', component: TransactionComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'credit', component: CreditComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'debit', component: DebitComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'balance', component: BalanceComponent,
    canActivate: [AuthGuardService]
  }
]


@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot( routes )
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }
