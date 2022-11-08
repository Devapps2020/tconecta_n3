package com.blm.qiubopay.utils;

import android.Manifest;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import com.blm.qiubopay.activities.LoginActivity;

import org.junit.Rule;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public abstract class HBase {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityScenarioRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule permissionCamera = GrantPermissionRule.grant(Manifest.permission.CAMERA);
    @Rule
    public GrantPermissionRule permissionAudio = GrantPermissionRule.grant(Manifest.permission.RECORD_AUDIO);
    @Rule
    public GrantPermissionRule permissionLocation = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule
    public GrantPermissionRule permissionRead = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule permissionWrite = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    public GrantPermissionRule permissionManage = GrantPermissionRule.grant(Manifest.permission.MANAGE_EXTERNAL_STORAGE);

}
