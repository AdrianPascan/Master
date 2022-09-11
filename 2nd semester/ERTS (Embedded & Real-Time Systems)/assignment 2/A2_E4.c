/* USER CODE BEGIN Header */
/**
  ******************************************************************************
  * @file           : main.c
  * @brief          : Main program body
  ******************************************************************************
  * @attention
  *
  * Copyright (c) 2022 STMicroelectronics.
  * All rights reserved.
  *
  * This software is licensed under terms that can be found in the LICENSE file
  * in the root directory of this software component.
  * If no LICENSE file comes with this software, it is provided AS-IS.
  *
  ******************************************************************************
  */
/* USER CODE END Header */
/* Includes ------------------------------------------------------------------*/
#include "main.h"
#include "cmsis_os.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */
#include <stdio.h>
/* USER CODE END Includes */

/* Private typedef -----------------------------------------------------------*/
/* USER CODE BEGIN PTD */

/* USER CODE END PTD */

/* Private define ------------------------------------------------------------*/
/* USER CODE BEGIN PD */
/* USER CODE END PD */

/* Private macro -------------------------------------------------------------*/
/* USER CODE BEGIN PM */

/* USER CODE END PM */

/* Private variables ---------------------------------------------------------*/
SPI_HandleTypeDef hspi1;

UART_HandleTypeDef huart2;

/* Definitions for defaultTask */
osThreadId_t defaultTaskHandle;
const osThreadAttr_t defaultTask_attributes = {
  .name = "defaultTask",
  .stack_size = 128 * 4,
  .priority = (osPriority_t) osPriorityNormal,
};
/* Definitions for greenLedTask */
osThreadId_t greenLedTaskHandle;
const osThreadAttr_t greenLedTask_attributes = {
  .name = "greenLedTask",
  .stack_size = 512 * 4,
  .priority = (osPriority_t) osPriorityHigh7,
};
/* Definitions for orangeLedTask */
osThreadId_t orangeLedTaskHandle;
const osThreadAttr_t orangeLedTask_attributes = {
  .name = "orangeLedTask",
  .stack_size = 512 * 4,
  .priority = (osPriority_t) osPriorityHigh6,
};
/* Definitions for redLedTask */
osThreadId_t redLedTaskHandle;
const osThreadAttr_t redLedTask_attributes = {
  .name = "redLedTask",
  .stack_size = 512 * 4,
  .priority = (osPriority_t) osPriorityHigh5,
};
/* Definitions for blueLedTask */
osThreadId_t blueLedTaskHandle;
const osThreadAttr_t blueLedTask_attributes = {
  .name = "blueLedTask",
  .stack_size = 512 * 4,
  .priority = (osPriority_t) osPriorityHigh4,
};
/* Definitions for ledMutex */
osMutexId_t ledMutexHandle;
const osMutexAttr_t ledMutex_attributes = {
  .name = "ledMutex"
};
/* USER CODE BEGIN PV */

/* USER CODE END PV */

/* Private function prototypes -----------------------------------------------*/
void SystemClock_Config(void);
static void MX_GPIO_Init(void);
static void MX_SPI1_Init(void);
static void MX_USART2_UART_Init(void);
void StartDefaultTask(void *argument);
void GreenLedTask(void *argument);
void OrangeLedTask(void *argument);
void RedLedTask(void *argument);
void BlueLedTask(void *argument);

/* USER CODE BEGIN PFP */

/* USER CODE END PFP */

/* Private user code ---------------------------------------------------------*/
/* USER CODE BEGIN 0 */
void initializeLeds(void) {
	  HAL_GPIO_WritePin(LD4_GPIO_Port, LD4_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(LD3_GPIO_Port, LD3_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(LD5_GPIO_Port, LD5_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(LD6_GPIO_Port, LD6_Pin, GPIO_PIN_SET);
}

int __io_putchar(int ch) {
	HAL_UART_Transmit(&huart2, (uint8_t*) &ch, 1, HAL_MAX_DELAY); // sizeof(uint8_t) = 1
	return ch;
}
/* USER CODE END 0 */

/**
  * @brief  The application entry point.
  * @retval int
  */
int main(void)
{
  /* USER CODE BEGIN 1 */

  /* USER CODE END 1 */

  /* MCU Configuration--------------------------------------------------------*/

  /* Reset of all peripherals, Initializes the Flash interface and the Systick. */
  HAL_Init();

  /* USER CODE BEGIN Init */

  /* USER CODE END Init */

  /* Configure the system clock */
  SystemClock_Config();

  /* USER CODE BEGIN SysInit */

  /* USER CODE END SysInit */

  /* Initialize all configured peripherals */
  MX_GPIO_Init();
  MX_SPI1_Init();
  MX_USART2_UART_Init();
  /* USER CODE BEGIN 2 */
  initializeLeds();
  /* USER CODE END 2 */

  /* Init scheduler */
  osKernelInitialize();
  /* Create the mutex(es) */
  /* creation of ledMutex */
  ledMutexHandle = osMutexNew(&ledMutex_attributes);

  /* USER CODE BEGIN RTOS_MUTEX */
  /* add mutexes, ... */
  /* USER CODE END RTOS_MUTEX */

  /* USER CODE BEGIN RTOS_SEMAPHORES */
  /* add semaphores, ... */
  /* USER CODE END RTOS_SEMAPHORES */

  /* USER CODE BEGIN RTOS_TIMERS */
  /* start timers, add new ones, ... */
  /* USER CODE END RTOS_TIMERS */

  /* USER CODE BEGIN RTOS_QUEUES */
  /* add queues, ... */
  /* USER CODE END RTOS_QUEUES */

  /* Create the thread(s) */
  /* creation of defaultTask */
  defaultTaskHandle = osThreadNew(StartDefaultTask, NULL, &defaultTask_attributes);

  /* creation of greenLedTask */
  greenLedTaskHandle = osThreadNew(GreenLedTask, NULL, &greenLedTask_attributes);

  /* creation of orangeLedTask */
  orangeLedTaskHandle = osThreadNew(OrangeLedTask, NULL, &orangeLedTask_attributes);

  /* creation of redLedTask */
  redLedTaskHandle = osThreadNew(RedLedTask, NULL, &redLedTask_attributes);

  /* creation of blueLedTask */
  blueLedTaskHandle = osThreadNew(BlueLedTask, NULL, &blueLedTask_attributes);

  /* USER CODE BEGIN RTOS_THREADS */
  /* add threads, ... */
  /* USER CODE END RTOS_THREADS */

  /* USER CODE BEGIN RTOS_EVENTS */
  /* add events, ... */
  /* USER CODE END RTOS_EVENTS */

  /* Start scheduler */
  osKernelStart();

  /* We should never get here as control is now taken by the scheduler */
  /* Infinite loop */
  /* USER CODE BEGIN WHILE */
  while (1)
  {
    /* USER CODE END WHILE */

    /* USER CODE BEGIN 3 */
  }
  /* USER CODE END 3 */
}

/**
  * @brief System Clock Configuration
  * @retval None
  */
void SystemClock_Config(void)
{
  RCC_OscInitTypeDef RCC_OscInitStruct = {0};
  RCC_ClkInitTypeDef RCC_ClkInitStruct = {0};

  /** Configure the main internal regulator output voltage
  */
  __HAL_RCC_PWR_CLK_ENABLE();
  __HAL_PWR_VOLTAGESCALING_CONFIG(PWR_REGULATOR_VOLTAGE_SCALE1);
  /** Initializes the RCC Oscillators according to the specified parameters
  * in the RCC_OscInitTypeDef structure.
  */
  RCC_OscInitStruct.OscillatorType = RCC_OSCILLATORTYPE_HSE;
  RCC_OscInitStruct.HSEState = RCC_HSE_ON;
  RCC_OscInitStruct.PLL.PLLState = RCC_PLL_ON;
  RCC_OscInitStruct.PLL.PLLSource = RCC_PLLSOURCE_HSE;
  RCC_OscInitStruct.PLL.PLLM = 8;
  RCC_OscInitStruct.PLL.PLLN = 336;
  RCC_OscInitStruct.PLL.PLLP = RCC_PLLP_DIV2;
  RCC_OscInitStruct.PLL.PLLQ = 7;
  if (HAL_RCC_OscConfig(&RCC_OscInitStruct) != HAL_OK)
  {
    Error_Handler();
  }
  /** Initializes the CPU, AHB and APB buses clocks
  */
  RCC_ClkInitStruct.ClockType = RCC_CLOCKTYPE_HCLK|RCC_CLOCKTYPE_SYSCLK
                              |RCC_CLOCKTYPE_PCLK1|RCC_CLOCKTYPE_PCLK2;
  RCC_ClkInitStruct.SYSCLKSource = RCC_SYSCLKSOURCE_PLLCLK;
  RCC_ClkInitStruct.AHBCLKDivider = RCC_SYSCLK_DIV1;
  RCC_ClkInitStruct.APB1CLKDivider = RCC_HCLK_DIV4;
  RCC_ClkInitStruct.APB2CLKDivider = RCC_HCLK_DIV2;

  if (HAL_RCC_ClockConfig(&RCC_ClkInitStruct, FLASH_LATENCY_5) != HAL_OK)
  {
    Error_Handler();
  }
}

/**
  * @brief SPI1 Initialization Function
  * @param None
  * @retval None
  */
static void MX_SPI1_Init(void)
{

  /* USER CODE BEGIN SPI1_Init 0 */

  /* USER CODE END SPI1_Init 0 */

  /* USER CODE BEGIN SPI1_Init 1 */

  /* USER CODE END SPI1_Init 1 */
  /* SPI1 parameter configuration*/
  hspi1.Instance = SPI1;
  hspi1.Init.Mode = SPI_MODE_MASTER;
  hspi1.Init.Direction = SPI_DIRECTION_2LINES;
  hspi1.Init.DataSize = SPI_DATASIZE_8BIT;
  hspi1.Init.CLKPolarity = SPI_POLARITY_LOW;
  hspi1.Init.CLKPhase = SPI_PHASE_1EDGE;
  hspi1.Init.NSS = SPI_NSS_SOFT;
  hspi1.Init.BaudRatePrescaler = SPI_BAUDRATEPRESCALER_2;
  hspi1.Init.FirstBit = SPI_FIRSTBIT_MSB;
  hspi1.Init.TIMode = SPI_TIMODE_DISABLE;
  hspi1.Init.CRCCalculation = SPI_CRCCALCULATION_DISABLE;
  hspi1.Init.CRCPolynomial = 10;
  if (HAL_SPI_Init(&hspi1) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN SPI1_Init 2 */

  /* USER CODE END SPI1_Init 2 */

}

/**
  * @brief USART2 Initialization Function
  * @param None
  * @retval None
  */
static void MX_USART2_UART_Init(void)
{

  /* USER CODE BEGIN USART2_Init 0 */

  /* USER CODE END USART2_Init 0 */

  /* USER CODE BEGIN USART2_Init 1 */

  /* USER CODE END USART2_Init 1 */
  huart2.Instance = USART2;
  huart2.Init.BaudRate = 4800;
  huart2.Init.WordLength = UART_WORDLENGTH_8B;
  huart2.Init.StopBits = UART_STOPBITS_1;
  huart2.Init.Parity = UART_PARITY_NONE;
  huart2.Init.Mode = UART_MODE_TX_RX;
  huart2.Init.HwFlowCtl = UART_HWCONTROL_NONE;
  huart2.Init.OverSampling = UART_OVERSAMPLING_16;
  if (HAL_UART_Init(&huart2) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN USART2_Init 2 */

  /* USER CODE END USART2_Init 2 */

}

/**
  * @brief GPIO Initialization Function
  * @param None
  * @retval None
  */
static void MX_GPIO_Init(void)
{
  GPIO_InitTypeDef GPIO_InitStruct = {0};

  /* GPIO Ports Clock Enable */
  __HAL_RCC_GPIOE_CLK_ENABLE();
  __HAL_RCC_GPIOC_CLK_ENABLE();
  __HAL_RCC_GPIOH_CLK_ENABLE();
  __HAL_RCC_GPIOA_CLK_ENABLE();
  __HAL_RCC_GPIOB_CLK_ENABLE();
  __HAL_RCC_GPIOD_CLK_ENABLE();

  /*Configure GPIO pin Output Level */
  HAL_GPIO_WritePin(CS_I2C_SPI_GPIO_Port, CS_I2C_SPI_Pin, GPIO_PIN_RESET);

  /*Configure GPIO pin Output Level */
  HAL_GPIO_WritePin(OTG_FS_PowerSwitchOn_GPIO_Port, OTG_FS_PowerSwitchOn_Pin, GPIO_PIN_SET);

  /*Configure GPIO pin Output Level */
  HAL_GPIO_WritePin(GPIOD, LD4_Pin|LD3_Pin|LD5_Pin|LD6_Pin
                          |Audio_RST_Pin, GPIO_PIN_RESET);

  /*Configure GPIO pin : CS_I2C_SPI_Pin */
  GPIO_InitStruct.Pin = CS_I2C_SPI_Pin;
  GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_LOW;
  HAL_GPIO_Init(CS_I2C_SPI_GPIO_Port, &GPIO_InitStruct);

  /*Configure GPIO pin : OTG_FS_PowerSwitchOn_Pin */
  GPIO_InitStruct.Pin = OTG_FS_PowerSwitchOn_Pin;
  GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_LOW;
  HAL_GPIO_Init(OTG_FS_PowerSwitchOn_GPIO_Port, &GPIO_InitStruct);

  /*Configure GPIO pin : B1_Pin */
  GPIO_InitStruct.Pin = B1_Pin;
  GPIO_InitStruct.Mode = GPIO_MODE_IT_RISING;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  HAL_GPIO_Init(B1_GPIO_Port, &GPIO_InitStruct);

  /*Configure GPIO pin : BOOT1_Pin */
  GPIO_InitStruct.Pin = BOOT1_Pin;
  GPIO_InitStruct.Mode = GPIO_MODE_INPUT;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  HAL_GPIO_Init(BOOT1_GPIO_Port, &GPIO_InitStruct);

  /*Configure GPIO pins : LD4_Pin LD3_Pin LD5_Pin LD6_Pin
                           Audio_RST_Pin */
  GPIO_InitStruct.Pin = LD4_Pin|LD3_Pin|LD5_Pin|LD6_Pin
                          |Audio_RST_Pin;
  GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_LOW;
  HAL_GPIO_Init(GPIOD, &GPIO_InitStruct);

  /*Configure GPIO pin : OTG_FS_OverCurrent_Pin */
  GPIO_InitStruct.Pin = OTG_FS_OverCurrent_Pin;
  GPIO_InitStruct.Mode = GPIO_MODE_INPUT;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  HAL_GPIO_Init(OTG_FS_OverCurrent_GPIO_Port, &GPIO_InitStruct);

  /*Configure GPIO pin : MEMS_INT2_Pin */
  GPIO_InitStruct.Pin = MEMS_INT2_Pin;
  GPIO_InitStruct.Mode = GPIO_MODE_EVT_RISING;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  HAL_GPIO_Init(MEMS_INT2_GPIO_Port, &GPIO_InitStruct);

}

/* USER CODE BEGIN 4 */

/* USER CODE END 4 */

/* USER CODE BEGIN Header_StartDefaultTask */
/**
  * @brief  Function implementing the defaultTask thread.
  * @param  argument: Not used
  * @retval None
  */
/* USER CODE END Header_StartDefaultTask */
void StartDefaultTask(void *argument)
{
  /* USER CODE BEGIN 5 */
  /* Infinite loop */
  for(;;)
  {
    osDelay(1);
  }
  /* USER CODE END 5 */
}

/* USER CODE BEGIN Header_GreenLedTask */
/**
* @brief Function implementing the greenLedTask thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_GreenLedTask */
void GreenLedTask(void *argument)
{
  /* USER CODE BEGIN GreenLedTask */
  /* Infinite loop */
	int greenCount = 0;
	while (1) {
		if (++greenCount == 100) {
			HAL_GPIO_WritePin(LD4_GPIO_Port, LD4_Pin, GPIO_PIN_RESET);

			osMutexAcquire(ledMutexHandle, 1000);
			printf("green [\r\n");
			osMutexRelease(ledMutexHandle);
			osMutexAcquire(ledMutexHandle, 1000);
			printf("green led off\r\n");
			osMutexRelease(ledMutexHandle);
			osMutexAcquire(ledMutexHandle, 1000);
			printf("] green\r\n");
			osMutexRelease(ledMutexHandle);
		} else if (greenCount == 200) {
			greenCount = 0;
			HAL_GPIO_WritePin(LD4_GPIO_Port, LD4_Pin, GPIO_PIN_SET);

			osMutexAcquire(ledMutexHandle, 1000);
			printf("green [\r\n");
			osMutexRelease(ledMutexHandle);
			osMutexAcquire(ledMutexHandle, 1000);
			printf("green led on\r\n");
			osMutexRelease(ledMutexHandle);
			osMutexAcquire(ledMutexHandle, 1000);
			printf("] green\r\n");
			osMutexRelease(ledMutexHandle);
		}
		osDelay(1);
	}
  /* USER CODE END GreenLedTask */
}

/* USER CODE BEGIN Header_OrangeLedTask */
/**
* @brief Function implementing the orangeLedTask thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_OrangeLedTask */
void OrangeLedTask(void *argument)
{
  /* USER CODE BEGIN OrangeLedTask */
  /* Infinite loop */
	int orangeCount = 0;
	while (1) {
		if (++orangeCount == 150) {
			HAL_GPIO_WritePin(LD3_GPIO_Port, LD3_Pin, GPIO_PIN_RESET);

			osMutexAcquire(ledMutexHandle, 1000);
			printf("orange [\r\n");
			osMutexRelease(ledMutexHandle);
			osMutexAcquire(ledMutexHandle, 1000);
			printf("orange led off\r\n");
			osMutexRelease(ledMutexHandle);
			osMutexAcquire(ledMutexHandle, 1000);
			printf("] orange\r\n");
			osMutexRelease(ledMutexHandle);
		} else if (orangeCount == 300) {
			orangeCount = 0;
			HAL_GPIO_WritePin(LD3_GPIO_Port, LD3_Pin, GPIO_PIN_SET);

			osMutexAcquire(ledMutexHandle, 1000);
			printf("orange [\r\n");
			osMutexRelease(ledMutexHandle);
			osMutexAcquire(ledMutexHandle, 1000);
			printf("orange led on\r\n");
			osMutexRelease(ledMutexHandle);
			osMutexAcquire(ledMutexHandle, 1000);
			printf("] orange\r\n");
			osMutexRelease(ledMutexHandle);
		}
		osDelay(1);
	}
  /* USER CODE END OrangeLedTask */
}

/* USER CODE BEGIN Header_RedLedTask */
/**
* @brief Function implementing the redLedTask thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_RedLedTask */
void RedLedTask(void *argument)
{
  /* USER CODE BEGIN RedLedTask */
  /* Infinite loop */
	int redCount = 0;
	while (1) {
		if (++redCount == 250) {
			HAL_GPIO_WritePin(LD5_GPIO_Port, LD5_Pin, GPIO_PIN_RESET);

			osMutexAcquire(ledMutexHandle, 1000);
			printf("red [\r\n");
			osMutexRelease(ledMutexHandle);
			osMutexAcquire(ledMutexHandle, 1000);
			printf("red led off\r\n");
			osMutexRelease(ledMutexHandle);
			osMutexAcquire(ledMutexHandle, 1000);
			printf("] red\r\n");
			osMutexRelease(ledMutexHandle);
		} else if (redCount == 500) {
			redCount = 0;
			HAL_GPIO_WritePin(LD5_GPIO_Port, LD5_Pin, GPIO_PIN_SET);

			osMutexAcquire(ledMutexHandle, 1000);
			printf("red [\r\n");
			osMutexRelease(ledMutexHandle);
			osMutexAcquire(ledMutexHandle, 1000);
			printf("red led on\r\n");
			osMutexRelease(ledMutexHandle);
			osMutexAcquire(ledMutexHandle, 1000);
			printf("] red\r\n");
			osMutexRelease(ledMutexHandle);
		}
		osDelay(1);
	}
  /* USER CODE END RedLedTask */
}

/* USER CODE BEGIN Header_BlueLedTask */
/**
* @brief Function implementing the blueLedTask thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_BlueLedTask */
void BlueLedTask(void *argument)
{
  /* USER CODE BEGIN BlueLedTask */
  /* Infinite loop */
	int blueCount = 0;
	while (1) {
		if (++blueCount == 400) {
			HAL_GPIO_WritePin(LD6_GPIO_Port, LD6_Pin, GPIO_PIN_RESET);

			osMutexAcquire(ledMutexHandle, 1000);
			printf("blue [\r\n");
			osMutexRelease(ledMutexHandle);
			osMutexAcquire(ledMutexHandle, 1000);
			printf("blue led off\r\n");
			osMutexRelease(ledMutexHandle);
			osMutexAcquire(ledMutexHandle, 1000);
			printf("] blue\r\n");
			osMutexRelease(ledMutexHandle);
		} else if (blueCount == 800) {
			blueCount = 0;
			HAL_GPIO_WritePin(LD6_GPIO_Port, LD6_Pin, GPIO_PIN_SET);

			osMutexAcquire(ledMutexHandle, 1000);
			printf("blue [\r\n");
			osMutexRelease(ledMutexHandle);
			osMutexAcquire(ledMutexHandle, 1000);
			printf("blue led on\r\n");
			osMutexRelease(ledMutexHandle);
			osMutexAcquire(ledMutexHandle, 1000);
			printf("] blue\r\n");
			osMutexRelease(ledMutexHandle);
		}
		osDelay(1);
	}
  /* USER CODE END BlueLedTask */
}

/**
  * @brief  This function is executed in case of error occurrence.
  * @retval None
  */
void Error_Handler(void)
{
  /* USER CODE BEGIN Error_Handler_Debug */
  /* User can add his own implementation to report the HAL error return state */
  __disable_irq();
  while (1)
  {
	  HAL_Delay(1000);
  }
  /* USER CODE END Error_Handler_Debug */
}

#ifdef  USE_FULL_ASSERT
/**
  * @brief  Reports the name of the source file and the source line number
  *         where the assert_param error has occurred.
  * @param  file: pointer to the source file name
  * @param  line: assert_param error line source number
  * @retval None
  */
void assert_failed(uint8_t *file, uint32_t line)
{
  /* USER CODE BEGIN 6 */
  /* User can add his own implementation to report the file name and line number,
     ex: printf("Wrong parameters value: file %s on line %d\r\n", file, line) */
  /* USER CODE END 6 */
}
#endif /* USE_FULL_ASSERT */

