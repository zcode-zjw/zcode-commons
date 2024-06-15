local key = KEYS[1]
local threshold = tonumber(ARGV[1])
local ttl = tonumber(ARGV[2])

local current = tonumber(redis.call('get', key))

if current == nil then
    redis.call("setex", key , ttl , 1);
    return false
else
    if current+1 > threshold then
        return true
    else
        redis.call("incr", key)
        redis.call("expire", key , ttl)
        return false
    end
end
