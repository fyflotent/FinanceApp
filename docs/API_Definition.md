### `/stocks`

#### GET

- Returns a list of stored stocks
- Example output: `["TSLA", "AAPL"]`

#### POST

- Adds a new stock object
- input: stock object
- example input: `{ "stock": "TSLA" }`
- example output: `"Got TSLA"`

### `/stocks/*id`

- params:
    - id: stock name to remove
        - example value `TSLA`

#### DELETE

- Delete the stock id from watch list
- example output: `Deleted TSLA`

### `/stocks/info`

#### GET

- Returns stock information for all watch stocks
- example output:
  ```json
  {
    "AAPL": {
      "price": 148.69,
      "low": 148.64,
      "high": 150.18
    },
    "TSLA": {
      "price": 909.68,
      "low": 891,
      "high": 910
    }
  }
  ```

### `/stocks/info/history/*id`

- params:
    - id: stock name to remove
        - example value `TSLA`

#### GET

- Returns historical info for the given stock
- example output:
    ```json
    {
      "2021-06-01 12:00:00": {
        "low": 571.219971,
        "high": 697.619995
      },
      ...
      "2021-10-01 12:00:00": {
        "low": 763.590027,
        "high": 910
      }
    }
    
    ```

### `/stocks/info/averages/*id`

- params:
    - id: stock name to remove
        - example value `TSLA`

#### GET

- Returns the average of the given stock
- Example output
    ```json
    {
      "average": 695.34406
    }
    ```

### `/stocks/info/averages`

#### GET

- Returns the averages for all watched stocks
- Example output
  ```json
  {
    "TSLA": 695.34406,
    "AAPL": 139.41185
  }
  ```

### `/stocks/info/socket`

Web socket which returns stock information on a timer

#### Definition

- Accepted Messages
    - `subscribe`
        - Triggers the websocket to start sending updates
    - `unsubscribe`
        - Unsubscribes all subscribed websockets
- Expected output messages
    - Stock information for all watched stocks
        - Example
          ```json
          {
            "TSLA": {
              "price": 909.68,
              "low": 891,
              "high": 910
            }
          }
          ```