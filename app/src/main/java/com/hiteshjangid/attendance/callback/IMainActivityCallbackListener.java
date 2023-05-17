package com.hiteshjangid.attendance.callback;

import com.hiteshjangid.attendance.model.Class_Names;

import java.util.List;

public interface IMainActivityCallbackListener {
    void onGradeLoadSuccess(List<Class_Names> class_names);
    void onGradeLoadFailed(String message);
}
