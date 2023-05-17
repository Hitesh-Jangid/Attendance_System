package com.hiteshjangid.attendance.callback;

import com.hiteshjangid.attendance.model.Grade_Names;

import java.util.List;

public interface IGradeCallbackListener {
    void onGradeLoadSuccess(List<Grade_Names> grade_list);
    void onGradeLoadFailed(String message);
}
