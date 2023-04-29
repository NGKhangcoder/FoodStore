package com.foodstore.client.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.foodstore.common.entity.setting.Setting;
import com.foodstore.common.entity.setting.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String> {
	
	public List<Setting> findByCategory(SettingCategory category);
	
}
