import { Routes } from '@angular/router';
import { authGuard } from './auth.guard';
import { ProjectList } from './project-list-component/project-list';
import { Login } from './login/login';

export const routes: Routes = [
    { path: 'projects', component: ProjectList, canActivate: [authGuard] },
    { path: 'login', component: Login },
    { path: '', redirectTo: '/projects', pathMatch: 'full' },
    { path: '**', redirectTo: '/projects', pathMatch: 'full' }
];  