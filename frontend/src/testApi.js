import { getAllSpaces } from './services/apiService';

async function test() {
  console.log('Testing API connection...');
  
  try {
    const spaces = await getAllSpaces();
    console.log('✅ SUCCESS! Got', spaces.length, 'spaces');
    console.log('First space:', spaces[0]);
  } catch (error) {
    console.log('❌ ERROR:', error.message);
  }
}

test();