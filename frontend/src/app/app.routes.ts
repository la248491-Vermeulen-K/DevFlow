import { Routes } from '@angular/router';
import { authGuard } from './auth.guard';
import { ProjectList } from './project-list-component/project-list';
import { Login } from './login/login';
import { ProfileComponent } from './profile-component/profile-component';
import { ProjectFormComponent } from './project-form-component/project-form-component';

export const routes: Routes = [
    { path: 'projects', children: [{ path: '', component: ProjectList}, { path: 'new', component: ProjectFormComponent}]},
    { path: 'login', component: Login },
    { path: 'profile', component: ProfileComponent, canActivate: [authGuard] },
    { path: '', redirectTo: '/projects', pathMatch: 'full' },
    { path: '**', redirectTo: '/projects', pathMatch: 'full' }
];  