import { Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import Login from './components/Login';
import Signup from './components/Signup';
import ApplyLeave from './components/ApplyLeave';
import LeaveHistory from './components/LeaveHistory';
import AssignTrainer from './components/AssignTrainer';
import AssignTrainees from './components/AssignTrainees';
import UpdateLeave from './components/UpdateLeave';
import ProtectedRoute from './components/ProtectedRoute';
import Home from './components/Home';

const App = () => {
    return (
        <>
            <Routes>
                <Route path="/" element={<Home/>} />
                <Route path="/login" element={<Login />} />
                <Route path="/signup" element={<Signup />} />
                <Route
                    path="/applyleave"
                    element={
                        <ProtectedRoute allowedRoles={['student']}>
                            <ApplyLeave />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/leave-history"
                    element={
                        <ProtectedRoute allowedRoles={['student']}>
                            <LeaveHistory />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/assign-trainer"
                    element={
                        <ProtectedRoute allowedRoles={['student']}>
                            <AssignTrainer />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/assign-trainees"
                    element={
                        <ProtectedRoute allowedRoles={['employee']}>
                            <AssignTrainees />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/update-leave"
                    element={
                        <ProtectedRoute allowedRoles={['employee']}>
                            <UpdateLeave />
                        </ProtectedRoute>
                    }
                />
            </Routes>
        </>
    );
};

export default App;