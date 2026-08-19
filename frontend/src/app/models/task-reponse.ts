import { Label } from "./label-response";

export interface Task{
    id: number,
    title: string,
    description: string | null,
    status: 'TODO' | 'IN_PROGRESS' | 'DONE',
    createdAt: string,
    assigneeId: number | null,
    assigneeName: string | null,
    deadline: string | null,
    priority: 'LOW' | 'MEDIUM' | 'HIGH',
    labels: Label[]
}