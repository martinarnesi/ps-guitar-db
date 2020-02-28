package com.guitar.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.guitar.db.model.ModelType;
import com.guitar.db.repository.ModelTypeJpaRepository;

@ContextConfiguration(locations={"classpath:com/guitar/db/applicationTests-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ModelTypePersistenceTests {
	@Autowired
	private ModelTypeJpaRepository modelTypeJpaRepository;
	
	@Test
	@Transactional
	public void testSaveAndGetAndDelete() throws Exception {
		ModelType mt = new ModelType();
		mt.setName("Test Model Type");
		mt = modelTypeJpaRepository.save(mt);

		ModelType otherModelType = modelTypeJpaRepository.findOne(mt.getId());
		assertEquals(mt.getName(), otherModelType.getName());
		
		modelTypeJpaRepository.delete(otherModelType);
	}

	@Test
	public void testFind() throws Exception {
		ModelType mt = modelTypeJpaRepository.findOne(1L);
		assertEquals("Dreadnought Acoustic", mt.getName());
	}
	
	@Test
	public void testFindAllJpa() throws Exception {
		List<ModelType> result = modelTypeJpaRepository.findAll();
		assertFalse(result.isEmpty());
	}
	
	@Test
	public void testFindNull() throws Exception {
		List<ModelType> mod = modelTypeJpaRepository.findByNameIsNull();
		assertTrue(!mod.isEmpty());
		assertEquals("8", mod.get(0).getId().toString());
		assertNull(mod.get(0).getName());
	}
}
