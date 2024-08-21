circuitbreaker service calls library service 


If library service is UP , CB stays in CLOSED state.

if library service is down , CB stays in OPEN state. It enters HALF-OPEN state automatically.

It goes back to OPEN state id Library service stays down.

It enters CLOSED state if Library service is UP.
